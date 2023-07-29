package ru.practicum.controllers.admins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.model.CommentsSearchParams;
import ru.practicum.comments.service.CommentService;

import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("/admin")
@Validated
public class AdminCommentController {
    private final CommentService service;

    @Autowired
    public AdminCommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/comments/{commentId}")
    public CommentOutDto getCommentById(@Positive(message = "{validation.commentIdPositive}")
                                        @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.getCommentByIdForAdmin(commentId);
        log.info("Получен комментарий для админа {}.", result);
        return result;
    }

    @GetMapping("/comments")
    public List<CommentOutDto> getAllComment(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                             @PastOrPresent(message = "{validation.rangeStartPastOrPresent}")
                                             @RequestParam(value = "rangeStart", required = false)
                                             LocalDateTime rangeStart,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                             @Future(message = "{validation.rangeEndFuture}")
                                             @RequestParam(value = "rangeEnd", required = false)
                                             LocalDateTime rangeEnd,
                                             @RequestParam(value = "status") Optional<String> status,
                                             @RequestParam(value = "userIds", required = false) List<Long> userIds,
                                             @RequestParam(value = "commentIds", required = false) List<Long> commentIds,
                                             @PositiveOrZero(message = "{validation.fromPositiveOrZero}")
                                             @RequestParam(value = "from", defaultValue = "0") int from,
                                             @Positive(message = "{validation.sizePositive}")
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentsSearchParams params = CommentsSearchParams.builder()
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .status(CommentStatus.from(status.orElse(null)))
                .userIds(userIds)
                .commentIds(commentIds)
                .from(from)
                .size(size)
                .build();
        List<CommentOutDto> result = service.getAllComment(params);
        log.info("Получены комментарий для администратора {}.", result);
        return result;
    }

    @PatchMapping("/comments")
    public List<CommentOutDto> updateCommentByIdForAdmin(@RequestParam(value = "status") String status,
                                                         @RequestParam(value = "commentIds") List<Long> commentIds) {
        List<CommentOutDto> result = service.updateCommentByIdForAdmin(commentIds, status);
        log.info("Изменен статус комментарий для админа {}.", result);
        return result;
    }
}
