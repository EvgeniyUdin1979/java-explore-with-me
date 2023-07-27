package ru.practicum.controllers.privates;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentInDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.dto.CommentUpdateInDto;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.model.CommentsPrivateSearchParams;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class PrivateCommentController {
    private final CommentService service;

    @Autowired
    public PrivateCommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping("/{userId}/comments")
    public CommentOutDto addComment(@Valid @RequestBody CommentInDto inDto,
                                    @Positive(message = "{validation.eventIdPositive}")
                                    @RequestParam(value = "eventId") long eventId,
                                    @Positive(message = "{validation.parentIdPositive}")
                                    @RequestParam(value = "parentId") Optional<Long> parentId,
                                    @Positive(message = "{validation.userIdPositive}")
                                    @PathVariable(value = "userId") long userId) {
        CommentOutDto result = service.addComment(inDto, eventId, parentId, userId);
        log.info("Добавлен комментарий {}.", result);
        return result;
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentOutDto updateComment(@Valid @RequestBody CommentUpdateInDto inDto,
                                       @Positive(message = "{validation.userIdPositive}")
                                       @PathVariable(value = "userId") long userId,
                                       @Positive(message = "{validation.commentIdPositive}")
                                       @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.updateComment(inDto, userId, commentId);
        log.info("Комментарий изменен пользователем {}.", result);
        return result;
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public CommentOutDto deleteComment(@Positive(message = "{validation.userIdPositive}")
                                       @PathVariable(value = "userId") long userId,
                                       @Positive(message = "{validation.commentIdPositive}")
                                       @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.deleteComment(userId, commentId);
        log.info("Комментарий удален пользователем {}.", result);
        return result;
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentOutDto getCommentById(@Positive(message = "{validation.userIdPositive}")
                                        @PathVariable(value = "userId") long userId,
                                        @Positive(message = "{validation.commentIdPositive}")
                                        @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.getCommentByIdForPrivate(userId, commentId);
        log.info("Получен комментарий для пользователем {}.", result);
        return result;
    }

    @GetMapping("/{userId}/comments")
    public List<CommentOutDto> getAllComment(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       @PastOrPresent(message = "{validation.rangeStartPastOrPresent}")
                                       @RequestParam(value = "rangeStart", required = false)
                                       LocalDateTime rangeStart,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       @Future(message = "{validation.rangeEndFuture}")
                                       @RequestParam(value = "rangeEnd", required = false)
                                       LocalDateTime rangeEnd,
                                       @RequestParam(value = "status") Optional<String> status,
                                       @Positive(message = "{validation.userIdPositive}")
                                       @PathVariable(value = "userId") long userId,
                                       @PositiveOrZero(message = "{validation.fromPositiveOrZero}")
                                       @RequestParam(value = "from", defaultValue = "0") int from,
                                       @Positive(message = "{validation.sizePositive}")
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentsPrivateSearchParams params = CommentsPrivateSearchParams.builder()
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .status(CommentStatus.from(status.orElse(null)))
                .userId(userId)
                .from(from)
                .size(size)
                .build();
        List<CommentOutDto> result = service.getAllCommentForPrivate(params);
        log.info("Получены комментарий для пользователем {}.", result);
        return result;
    }
}
