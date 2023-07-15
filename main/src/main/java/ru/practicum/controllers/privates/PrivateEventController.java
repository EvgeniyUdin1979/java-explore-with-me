package ru.practicum.controllers.privates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.service.EventService;
import ru.practicum.request.dto.ParticipationRequestOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class PrivateEventController {

    private final EventService service;

    @Autowired
    public PrivateEventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/{userId}/events")
    public EventOutShortDto addEvent(
            @Valid @RequestBody EventInDto inDto,
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userId) {
        EventOutShortDto result = service.add(inDto, userId);
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
            @RequestParam("from") int from,
            @Positive(message = "validation.sizePositive")
            @RequestParam("from") int size) {
        List<EventOutShortDto> result = service.getAllByUserId(userid, from, size);
        log.info("Получены события для пользователя с id {} : {}", userid, result);
        return result;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventOutFullDto getEventDyUserId(
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userid,
            @Positive(message = "validation.eventIdPositive")
            @PathVariable("eventId") long eventId) {
        EventOutFullDto result = service.getByUserId(userid, eventId);
        log.info("Получено событие с {} для пользователя с id {}", result, userid);
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
