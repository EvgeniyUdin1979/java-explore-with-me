package ru.practicum.controllers.privates;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.BaseClient;
import ru.practicum.client.SendParams;
import ru.practicum.events.dto.*;
import ru.practicum.events.service.EventService;
import ru.practicum.request.dto.ParticipationRequestOutDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
@Hidden
public class PrivateEventController {

    private final EventService service;
    private final BaseClient client;

    public PrivateEventController(EventService service, BaseClient client) {
        this.service = service;
        this.client = client;
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutFullDto addEvent(
            @Valid @RequestBody EventInDto inDto,
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userId) {
        EventOutFullDto result = service.add(inDto, userId);
        log.info("Добавлено мероприятие {}.", result);
        return result;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventOutFullDto updateEvent(
            @Valid @RequestBody EventUpdateInDto inDto,
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userid,
            @Positive(message = "{validation.eventIdPositive}")
            @PathVariable("eventId") long eventId) {
        EventOutFullDto result = service.update(inDto, userid, eventId);
        log.info("Обновлено мероприятие {}.", result);
        return result;
    }

    @GetMapping("/{userId}/events")
    public List<EventOutShortDto> getEventsByUserId(
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userid,
            @PositiveOrZero(message = "validation.fromPositiveOrZero")
            @RequestParam(value = "from", defaultValue = "0") int from,
            @Positive(message = "validation.sizePositive")
            @RequestParam(value = "size", defaultValue = "10") int size,
            HttpServletRequest servletRequest) {
        List<EventOutShortDto> result = service.getAllByUserId(userid, from, size);
        log.info("Получены события для пользователя с id {} : {}", userid, result);
        client.sendStats(SendParams.builder()
                .created(LocalDateTime.now())
                .ip(servletRequest.getRemoteAddr())
                .uri("/events")
                .app(servletRequest.getRequestURI())
                .build());
        return result;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventOutFullDto getEventByUserId(
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userid,
            @Positive(message = "validation.eventIdPositive")
            @PathVariable("eventId") long eventId,
            HttpServletRequest servletRequest) {
        EventOutFullDto result = service.getByUserId(userid, eventId);
        log.info("Получено событие с {} для пользователя с id {}", result, userid);
        client.sendStats(SendParams.builder()
                .created(LocalDateTime.now())
                .ip(servletRequest.getRemoteAddr())
                .uri("/events")
                .app(servletRequest.getRequestURI())
                .build());
        return result;

    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestOutDto> getRequestsInEventByUserId(
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userid,
            @Positive(message = "validation.eventIdPositive")
            @PathVariable("eventId") long eventId) {
        List<ParticipationRequestOutDto> result = service.getRequestsInEventByUserId(userid, eventId);
        log.info("Получено событие с {} для пользователя с id {}", result, userid);
        return result;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestsStatusUpdateOutDto updateRequestsInEventByUserId(
            @RequestBody EventRequestsStatusUpdateInDto inDto,
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userid,
            @Positive(message = "validation.eventIdPositive")
            @PathVariable("eventId") long eventId) {
        EventRequestsStatusUpdateOutDto result = service.updateRequestsInEventByUserId(inDto, userid, eventId);
        log.info("Получено событие с {} для пользователя с id {}", result, userid);
        return result;
    }


}
