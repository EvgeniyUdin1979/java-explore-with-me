package ru.practicum.controllers.admins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.constraints.Positive;


@RestController
@Slf4j
@RequestMapping("/users")
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

    @PatchMapping("/comments/{commentId}")
    public CommentOutDto updateCommentByIdForAdmin(@RequestParam(value = "status") String status,
                                                   @Positive(message = "{validation.commentIdPositive}")
                                                   @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.updateCommentByIdForAdmin(commentId, status);
        log.info("Изменен статус комментарий для админа {}.", result);
        return result;
    }


}
