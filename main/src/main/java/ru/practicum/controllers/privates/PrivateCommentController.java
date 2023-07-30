package ru.practicum.controllers.privates;


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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.advices.ApiError;
import ru.practicum.comments.dto.CommentInDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.model.CommentsSearchParams;
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
@Tag(
        name = "Взаимодействие с комментариями.",
        description = "Предоставляет возможность взаимодействие зарегистрированных пользователей с функциями комментариев.")
public class PrivateCommentController {
    private final CommentService service;

    @Autowired
    public PrivateCommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping(value = "/{userId}/comments", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Добавление комментария пользователем.",
            description = "Позволяет пользователю оставлять комментарии к мероприятию, " +
                    "а также участвовать в дискуссии оставляя комментарии к определенному комментарию(parentId). " +
                    "Событие или комментарий к которому добавляется комментарий должны иметь состояние опубликован(PUBLISHED)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CommentOutDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "4xx",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = "application/json"))
    })
    public CommentOutDto addComment(@Valid @RequestBody CommentInDto inDto,
                                    @Positive(message = "{validation.eventIdPositive}")
                                    @RequestParam(value = "eventId") long eventId,
                                    @RequestParam(value = "parentId") Optional<Long> parentId,
                                    @Positive(message = "{validation.userIdPositive}")
                                    @PathVariable(value = "userId") long userId) {

        CommentOutDto result = service.addComment(inDto, eventId, parentId, userId);
        log.info("Добавлен комментарий {}.", result);
        return result;
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    @Operation(
            summary = "Изменение комментария пользователем.",
            description = "Позволяет пользователю изменять комментарии к мероприятию." +
                    "Пользователь может изменить текст комментария при этом комментарий перейдет в состояние ожидает публикации(PENDING), " +
                    "но только в случае если он был опубликован(PUBLISHED). Удаленные(DELETED) или отклоненные(REJECT) комментарии можно изменять и при необходимости сменить состояние на ожидает публикации(PENDING). " +
                    "Пользователь не может менять состояние на удален(DELETED), для этого есть ендпоинт удаления."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CommentOutDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "4xx",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = "application/json"))
    })
    public CommentOutDto updateComment(@RequestParam(value = "text", required = false) String text,
                                       @Positive(message = "{validation.userIdPositive}")
                                       @PathVariable(value = "userId") long userId,
                                       @RequestParam(value = "status", required = false) String status,
                                       @Positive(message = "{validation.commentIdPositive}")
                                       @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.updateComment(text, userId, commentId, status);
        log.info("Комментарий изменен пользователем {}.", result);
        return result;
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @Operation(
            summary = "Удаление комментария пользователем.",
            description = "Позволяет пользователю удалить комментарий к мероприятию. Он перейдет в статус удален(DELETED). В последующем его можно изменить и дать статус ожидает публикации(PENDING)." +
                    "Администратор не может публиковать удаленные комментарии, а также их нет при получении события."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CommentOutDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "4xx",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = "application/json"))
    })
    public CommentOutDto deleteComment(@Positive(message = "{validation.userIdPositive}")
                                       @PathVariable(value = "userId") long userId,
                                       @Positive(message = "{validation.commentIdPositive}")
                                       @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.deleteComment(userId, commentId);
        log.info("Комментарий удален пользователем {}.", result);
        return result;
    }

    @GetMapping("/{userId}/comments/{commentId}")
    @Operation(
            summary = "Получение своего комментария пользователем.",
            description = "Позволяет пользователю получить свой комментарий."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CommentOutDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "4xx",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = "application/json"))
    })
    public CommentOutDto getCommentById(@Positive(message = "{validation.userIdPositive}")
                                        @PathVariable(value = "userId") long userId,
                                        @Positive(message = "{validation.commentIdPositive}")
                                        @PathVariable(value = "commentId") long commentId) {
        CommentOutDto result = service.getCommentByIdForPrivate(userId, commentId);
        log.info("Получен комментарий для пользователем {}.", result);
        return result;
    }

    @GetMapping("/{userId}/comments")
    @Operation(
            summary = "Получение своих комментариев пользователем.",
            description = "Позволяет пользователю получить свои комментарии на основе заданных условий."
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
                                       @RequestParam(value = "status", required = false) Optional<String> status,
                                       @Positive(message = "{validation.userIdPositive}")
                                       @PathVariable(value = "userId") long userId,
                                       @PositiveOrZero(message = "{validation.fromPositiveOrZero}")
                                       @RequestParam(value = "from", defaultValue = "0") int from,
                                       @Positive(message = "{validation.sizePositive}")
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentsSearchParams params = CommentsSearchParams.builder()
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .status(CommentStatus.from(status.orElse(null)))
                .userIds(List.of(userId))
                .from(from)
                .size(size)
                .build();
        List<CommentOutDto> result = service.getAllComment(params);
        log.info("Получены комментарий для пользователем {}.", result);
        return result;
    }
}
