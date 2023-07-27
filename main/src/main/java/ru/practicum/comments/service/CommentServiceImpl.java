package ru.practicum.comments.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentInDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.dto.CommentUpdateInDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.model.CommentsPrivateSearchParams;
import ru.practicum.comments.storage.CommentStorageDao;
import ru.practicum.comments.util.CommentMapper;
import ru.practicum.events.model.Event;
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
    public CommentOutDto addComment(CommentInDto inDto, long eventId, Optional<Long> parentId, long userId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        Comment parent = null;
        if (parentId.isPresent()) {
            parent = getCommentById(parentId.get());
        }
        Comment comment = CommentMapper.mapToEntity(inDto, event, user, parent);
        Comment result = commentStorage.add(comment);
        return CommentMapper.mapToOut(result);
    }

    @Override
    public CommentOutDto updateComment(CommentUpdateInDto inDto, long userId, long commentId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (comment.getCreator().getId() != userId) {
            String message = String.format("Комментарий с id %d не создавал пользователь с id %d.", commentId, userId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        if (inDto.getStatus() != null) {
            if (comment.getStatus() == CommentStatus.DELETED || comment.getStatus() == CommentStatus.REJECT) {
                String message = "Пользователь может поменять статус только у удаленного или отклоненного комментария.";
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            if (inDto.getStatus() != CommentStatus.PENDING) {
                String message = String.format("Пользователь может поменять статус только на 'Ожидает публикации(PENDING)' статус в запросе %s.", inDto.getStatus());
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
        }
        if (inDto.getText() != null && !inDto.getText().isBlank()) {
            String message = "Текст комментария не может быть пустым или содержать пробелы.";
            log.warn(message);
            throw new RequestException(message, HttpStatus.BAD_REQUEST, errorRequest);
        }
        Comment updateComment = CommentMapper.mapUpdateToEntity(inDto, comment);
        Comment result = commentStorage.update(updateComment);
        return CommentMapper.mapToOut(result);
    }

    @Override
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
    public List<CommentOutDto> getAllCommentForPrivate(CommentsPrivateSearchParams params) {
        getUserById(params.getUserId());
        List<Comment> result = commentStorage.getAllCommentForPrivate(params);
        return result.stream()
                .map(CommentMapper::mapToOut)
                .collect(Collectors.toList());
    }

    @Override
    public CommentOutDto updateCommentByIdForAdmin(long commentId, String status) {
        Comment comment = getCommentById(commentId);
        CommentStatus newestStatus = CommentStatus.from(status).orElseThrow(() -> {
            String message = String.format("Новый статус для комментария с id %d указан не верно %s.", commentId, status);
            log.warn(message);
            return new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        });
        if (comment.getStatus() == newestStatus) {
            String message = String.format("Новый статус для комментария с id %d совпадает с текущим %s | статус комментария %s.", commentId, status, comment.getStatus());
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        if (newestStatus !=  CommentStatus.PUBLISHED || newestStatus != CommentStatus.REJECT) {
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
        if (comment.getPublishedOn() != null) {
            comment.setPublishedOn(LocalDateTime.now());
        }
        Comment result = commentStorage.update(comment);
        return CommentMapper.mapToOut(result);
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
