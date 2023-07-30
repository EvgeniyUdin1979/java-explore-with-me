package ru.practicum.comments.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentInDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.model.CommentsSearchParams;
import ru.practicum.comments.storage.CommentStorageDao;
import ru.practicum.comments.util.CommentMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.storage.EventStorageDao;
import ru.practicum.exceptions.RequestException;
import ru.practicum.users.model.User;
import ru.practicum.users.storage.UserStorageDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final String errorRequest = "Запрос содержит не корректные данные";

    private final CommentStorageDao commentStorage;

    private final EventStorageDao eventStorage;

    private final UserStorageDao userStorage;

    @Autowired
    public CommentServiceImpl(CommentStorageDao commentStorage,
                              EventStorageDao eventStorage,
                              UserStorageDao userStorage) {
        this.commentStorage = commentStorage;
        this.eventStorage = eventStorage;
        this.userStorage = userStorage;
    }

    @Override
    @Transactional
    public CommentOutDto addComment(CommentInDto inDto, long eventId, Optional<Long> parentId, long userId) {
        User user = getUserById(userId);
        Comment parent = null;
        if (parentId.isPresent()) {
            long parentLong = parentId.get();
            if (parentLong < 1) {
                String message = String.format("id родительского комментария должен быть положительный. Передан: %d", parentLong);
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            getCommentById(parentLong);
            parent = getCommentById(parentLong);
            if (parent.getStatus() != CommentStatus.PUBLISHED) {
                String message = "Нельзя добавлять комментария если родительский комментарий не опубликован.";
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
        }
        Event event = getEventById(eventId);
        if (event.getState() != State.PUBLISHED) {
            String message = "Нельзя добавлять комментария если событие не опубликовано.";
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        Comment comment = CommentMapper.mapToEntity(inDto, event, user, parent);
        Comment result = commentStorage.add(comment);
        return CommentMapper.mapToOut(result);
    }

    @Override
    @Transactional
    public CommentOutDto updateComment(String text, long userId, long commentId, String status) {
        CommentStatus newestStatus = null;
        if (status != null) {
            newestStatus = CommentStatus.from(status).orElseThrow(() -> {
                String message = String.format("Новый статус для комментария с id %d указан не верно %s.", commentId, status);
                log.warn(message);
                return new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            });
        }

        getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (comment.getCreator().getId() != userId) {
            String message = String.format("Комментарий с id %d не создавал пользователь с id %d.", commentId, userId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        if (status != null) {
            if (comment.getStatus() == newestStatus) {
                String message = "Комментарий уже находится в текущем статусе.";
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            if (newestStatus != CommentStatus.PENDING) {
                String message = String.format("Пользователь может поменять статус только на 'Ожидает публикации (PENDING)', статус в запросе %s.", newestStatus);
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
        }
        if (text != null && text.isBlank()) {
            String message = "Текст комментария не может быть пустым или содержать пробелы.";
            log.warn(message);
            throw new RequestException(message, HttpStatus.BAD_REQUEST, errorRequest);
        }
        Comment updateComment = CommentMapper.mapUpdateToEntity(text, newestStatus, comment);
        Comment result = commentStorage.update(updateComment);
        return CommentMapper.mapToOut(result);
    }

    @Override
    @Transactional
    public CommentOutDto deleteComment(long userId, long commentId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (comment.getCreator().getId() != userId) {
            String message = String.format("Комментарий с id %d не создавал пользователь с id %d.", commentId, userId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        if (comment.getStatus() == CommentStatus.DELETED) {
            String message = String.format("Комментарий с id %d уже удален.", commentId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        comment.setStatus(CommentStatus.DELETED);
        Comment result = commentStorage.update(comment);
        return CommentMapper.mapToOut(result);
    }

    @Override
    @Transactional
    public CommentOutDto getCommentByIdForPrivate(long userId, long commentId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (comment.getCreator().getId() != userId) {
            String message = String.format("Комментарий с id %d не создавал пользователь с id %d.", commentId, userId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        return CommentMapper.mapToOut(comment);
    }

    @Override
    @Transactional
    public List<CommentOutDto> getAllComment(CommentsSearchParams params) {
        List<Comment> result = commentStorage.getAllComment(params);
        return result.stream()
                .map(CommentMapper::mapToOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CommentOutDto> updateCommentByIdForAdmin(List<Long> commentIds, String status) {
        CommentStatus newestStatus = CommentStatus.from(status).orElseThrow(() -> {
            String message = String.format("Новый статус для комментариев  указан не верно %s.", status);
            log.warn(message);
            return new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        });
        List<Comment> comments = commentStorage.getAllCommentsById(commentIds);
        comments.forEach(comment -> {
            if (comment.getStatus() == newestStatus) {
                String message = String.format("Новый статус для комментария с id %d совпадает с текущим %s | статус комментария %s.", comment.getId(), status, comment.getStatus());
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            if (newestStatus != CommentStatus.PUBLISHED && newestStatus != CommentStatus.REJECT) {
                String message = String.format("Администратор может только опубликовать(PUBLISHED) или отклонить(REJECT) комментарий. Пришел статус %s.", newestStatus);
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            if (comment.getStatus() != CommentStatus.PENDING) {
                String message = String.format("Опубликовать или отклонить можно только комментарии ожидающие публикации. Статус комментария %s.", comment.getStatus());
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            comment.setStatus(CommentStatus.PUBLISHED);
            if (comment.getPublishedOn() == null) {
                comment.setPublishedOn(LocalDateTime.now());
            }
        });
        List<Comment> result = commentStorage.updateAll(comments);
        return result.stream()
                .map(CommentMapper::mapToOut)
                .collect(Collectors.toList());
    }

    private User getUserById(long userId) {
        return userStorage.findById(userId).orElseThrow(() -> {
                    String message = String.format("Пользователь с id %d не найден.", userId);
                    log.warn(message);
                    return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
                }
        );
    }

    private Comment getCommentById(long commentId) {
        return commentStorage.findById(commentId).orElseThrow(() -> {
                    String message = String.format("Комментарий с id %d не найден.", commentId);
                    log.warn(message);
                    return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
                }
        );
    }

    @Override
    public CommentOutDto getCommentByIdForAdmin(long commentId) {
        Comment comment = getCommentById(commentId);
        return CommentMapper.mapToOut(comment);
    }

    private Event getEventById(long eventId) {
        return eventStorage.findById(eventId).orElseThrow(() -> {
            String message = String.format("Мероприятие с id %d не найдено.", eventId);
            log.warn(message);
            return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
        });

    }
}
