package ru.practicum.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.storage.EventStorageDao;
import ru.practicum.events.util.EventMapping;
import ru.practicum.exceptions.RequestException;
import ru.practicum.request.dto.ParticipationRequestOutDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.request.storage.RequestStorageDao;
import ru.practicum.users.model.User;
import ru.practicum.users.storage.UserStorageDao;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final String errorRequest = "Запрос содержит не корректные данные";

    private final RequestStorageDao requestStorage;
    private final UserStorageDao userStorage;
    private final EventStorageDao eventStorage;

    public RequestServiceImpl(RequestStorageDao requestStorage, UserStorageDao userStorage, EventStorageDao eventStorage) {
        this.requestStorage = requestStorage;
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
    }

    @Override
    @Transactional
    public List<ParticipationRequestOutDto> findAllRequestsByUserId(long userId) {
        getUserById(userId);
        List<Request> result = requestStorage.findAllByRequesterId(userId);
        return EventMapping.mapToRequestsDto(result);
    }

    @Override
    @Transactional
    public ParticipationRequestOutDto addRequestByUserId(long userId, long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        if (event.getInitiator().getId() == userId) {
            String message = "Регистрация запроса на участие в своем мероприятии не возможна.";
            log.warn(message);
            throw  new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        if (event.getState() != State.PUBLISHED) {
            String message = String.format("Мероприятие id %d не опубликовано. Регистрация запросов на участие не возможна.", eventId);
            log.warn(message);
            throw  new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        long count = event.getRequests().stream().filter(r -> r.getStatus() == Status.CONFIRMED).count();
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() - count <= 0) {
                String message = String.format("В мероприятии id %d достигнут лимит подтвержденных запросов на участие. Регистрация запросов на участие не возможна.", eventId);
                log.warn(message);
                throw  new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(getStatus(event))
                .build();

        Request result = requestStorage.add(request);
        return EventMapping.mapToRequestDto(result);
    }

    @Override
    @Transactional
    public ParticipationRequestOutDto patchRequestByUserId(long userId, long requestsId) {
        getUserById(userId);
        Request request = requestStorage.findById(requestsId).orElseThrow(() -> {
            String message = String.format("Запрос на участие с id %d не найден.", requestsId);
            log.warn(message);
            return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
        });
        if (request.getStatus() == Status.CANCELED) {
            String message = String.format("Запрос на участие с id %d уже отменён.", requestsId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
        }
        request.setStatus(Status.CANCELED);
        requestStorage.updateAllRequests(List.of(request));
        return EventMapping.mapToRequestDto(request);
    }

    private Status getStatus(Event event) {
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return Status.CONFIRMED;
        }
        return Status.PENDING;
    }


    private User getUserById(long userId) {
        return userStorage.findById(userId).orElseThrow(() -> {
                    String message = String.format("Пользователь с id %d не найден.", userId);
                    log.warn(message);
                    return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
                }
        );
    }


    private Event getEventById(long eventId) {
        return eventStorage.findById(eventId).orElseThrow(() -> {
            String message = String.format("Мероприятие с id %d не найдено.", eventId);
            log.warn(message);
            return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
        });

    }
}
