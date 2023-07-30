package ru.practicum.controllers.admins;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.advices.ApiError;
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
@Tag(
        name = "Администрирование комментариев",
        description = "Предоставляет администратору возможность получать требуемые комментарии и опубликовать их или отклонить.")
public class AdminCommentController {
    private final CommentService service;

    @Autowired
    public AdminCommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/comments/{commentId}")
    @Operation(
            summary = "Получение комментария администратором.",
            description = "Позволяет администратору получить требуемый комментарий по id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CommentOutDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "4xx",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = "application/json"))
    })
    public CommentOutDto getCommentById(@Positive(message = "{validation.commentIdPositive}")
                                        @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.getCommentByIdForAdmin(commentId);
        log.info("Получен комментарий для админа {}.", result);
        return result;
    }

    @GetMapping("/comments")
    @Operation(
            summary = "Получение комментариев администратором.",
            description = "Позволяет администратору получить комментарии на основе заданных параметров."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentOutDto.class)), mediaType = "application/json")),
            @ApiResponse(responseCode = "4xx",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = "application/json"))
    })
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
    @Operation(
            summary = "Обновляет состояние комментария.",
            description = "Позволяет администратору изменить состояние комментария на опубликован(PUBLISHED) или отклонен(REJECT). " +
                    "Для этого комментарий должен находиться в состоянии - ожидает публикации(PENDING)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentOutDto.class)), mediaType = "application/json")),
            @ApiResponse(responseCode = "4xx",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = "application/json"))
    })
    public List<CommentOutDto> updateCommentsByIdForAdmin(@RequestParam(value = "status") String status,
                                                          @RequestParam(value = "commentIds") List<Long> commentIds) {
        List<CommentOutDto> result = service.updateCommentByIdForAdmin(commentIds, status);
        log.info("Изменен статус комментарий для админа {}.", result);
        return result;
    }
}
