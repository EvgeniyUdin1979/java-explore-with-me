package ru.practicum.controllers.privates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestOutDto;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class PrivateRequestController {

    private final RequestService service;

    @Autowired
    public PrivateRequestController(RequestService service) {
        this.service = service;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestOutDto> getAllRequestByUserId(
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userId) {
        List<ParticipationRequestOutDto> result = service.findAllRequestsByUserId(userId);
        log.info("Получены запросы на мероприятия {}.", result);
        return result;
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestOutDto addRequestByUserId(
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userId,
            @Positive(message = "validation.eventIdPositive")
            @RequestParam("eventId") long eventId) {
        ParticipationRequestOutDto result = service.addRequestByUserId(userId, eventId);
        log.info(" запросы на мероприятия {}.", result);
        return result;
    }

    @PatchMapping("/{userId}/requests/{requestsId}/cancel")
    public ParticipationRequestOutDto cancelRequestByUserId(
            @Positive(message = "validation.userIdPositive")
            @PathVariable("userId") long userId,
            @Positive(message = "validation.requestIdPositive")
            @PathVariable("requestsId") long requestsId) {
        ParticipationRequestOutDto result = service.patchRequestByUserId(userId, requestsId);
        log.info(" запросы на мероприятия {}.", result);
        return result;
    }
}
