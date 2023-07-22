package ru.practicum.events.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.storage.CategoryStorageDao;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.StatParam;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.*;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventStorageDao eventStorage;
    private final UserStorageDao userStorage;
    private final CategoryStorageDao categoryStorage;
    private final RequestStorageDao requestStorage;
    private final BaseClient client;


    private final String errorRequest = "Запрос содержит не корректные данные";

    @Autowired
    public EventServiceImpl(EventStorageDao eventStorage,
                            UserStorageDao userStorage,
                            CategoryStorageDao categoryStorage,
                            RequestStorageDao requestStorage, BaseClient client) {
        this.eventStorage = eventStorage;
        this.userStorage = userStorage;
        this.categoryStorage = categoryStorage;
        this.requestStorage = requestStorage;
        this.client = client;
    }

    @Override
    @Transactional
    public EventOutFullDto add(EventInDto inDto, long userId) {
        User user = getUserById(userId);
        Category category = getCategoryById(inDto.getCategory());
        Event event = EventMapping.mapToEntity(inDto, user, category);
        Event result = eventStorage.add(event);
        long views = 0;
        return EventMapping.mapToFullDto(result, views);
    }

    @Override
    @Transactional
    public EventOutFullDto update(EventUpdateInDto inDto, long userid, long eventId) {
        Event event = getEventById(eventId);
        isOwnerEvent(userid, event);
        if (event.getState() == State.PUBLISHED) {
            String message = "Опубликованное мероприятие нельзя изменять.";
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, "Ошибка обработки ответа на добавления мероприятия.");
        }
        if (inDto.getStateAction() != null && inDto.getStateAction() == StateAction.PUBLISH_EVENT) {
            String message = "Опубликовать мероприятие может только администратор.";
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, "Ошибка обработки ответа на добавления мероприятия.");
        }
        Category category = null;
        if (inDto.getCategory() != null) {
            category = getCategoryById(inDto.getCategory());
        }
        Event updateEvent = EventMapping.mapToUpdateEntity(event, inDto, category);
        Event result = eventStorage.update(updateEvent);
        return viewsSupplerForFullDto(List.of(result)).stream().findFirst().orElseThrow(() -> {
            String message = "Не была получена статистика.";
            log.warn(message);
            return new RequestException(message, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка получения статистики.");
        });

    }


    @Override
    @Transactional
    public List<EventOutShortDto> getAllByUserId(long userid, int from, int size) {
        List<Event> result = eventStorage.findAllByUserId(userid, from, size);
        return viewsSupplerForShortDto(result);
    }


    @Override
    @Transactional
    public EventOutFullDto getByUserId(long userId, long eventId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        isOwnerEvent(userId, event);
        return viewsSupplerForFullDto(List.of(event)).stream().findFirst().orElseThrow(() -> {
            String message = "Не была получена статистика.";
            log.warn(message);
            return new RequestException(message, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка получения статистики.");
        });
    }


    @Override
    @Transactional
    public List<ParticipationRequestOutDto> getRequestsInEventByUserId(long userId, long eventId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        return EventMapping.mapToRequestsDto(new ArrayList<>(event.getRequests()));
    }

    @Override
    @Transactional
    public EventRequestsStatusUpdateOutDto updateRequestsInEventByUserId(
            EventRequestsStatusUpdateInDto inDto,
            long userid,
            long eventId) {
        getUserById(userid);
        Event event = getEventById(eventId);
        isOwnerEvent(userid, event);
        long remained = Long.MAX_VALUE;

        if (inDto.getStatus() == Status.CONFIRMED) {
            if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
                String message = String.format("В мероприятие с id %d отключена пре-модерация заявок или лимит заявок равен 0. Подтверждение заявок не требуется.", eventId);
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            long confirmed = event.getRequests().stream().filter(r -> r.getStatus() == Status.CONFIRMED).count();
            if (confirmed >= event.getParticipantLimit()) {
                String message = String.format("В мероприятие с id %d достигнут лимит подтвержденных заявок.", eventId);
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            remained = event.getParticipantLimit() - confirmed;
        }

        List<Request> requests = requestStorage.findAllRequestsById(inDto.getRequestIds());
        changeRequestStatus(requests, remained, inDto.getStatus());

        requestStorage.updateAllRequests(requests);
        Set<Request> requestSet = event.getRequests();
        List<ParticipationRequestOutDto> confirmedRequests = EventMapping.mapToRequestsDto(requestSet.stream()
                .filter(request -> request.getStatus() == Status.CONFIRMED)
                .collect(Collectors.toList()));
        List<ParticipationRequestOutDto> rejectedRequests = EventMapping.mapToRequestsDto(requestSet.stream()
                .filter(request -> request.getStatus() == Status.REJECTED)
                .collect(Collectors.toList()));
        return EventRequestsStatusUpdateOutDto.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }


    @Override
    @Transactional
    public List<EventOutFullDto> getAllForAdmin(EventAdminSearchParams params) {
        List<Event> result = eventStorage.findAllForAdmin(params);
        return viewsSupplerForFullDto(result);
    }

    @Override
    @Transactional
    public EventOutFullDto updateEventByAdmin(EventUpdateAdminInDto inDto, long eventId) {
        Event event = getEventById(eventId);
        if (inDto.getStateAction() != null) {

            if (inDto.getStateAction() == StateAction.PUBLISH_EVENT && event.getState() != State.PENDING) {
                String message = String.format("Мероприятие с id %d не имеет статуса 'Ожидает публикации'", eventId);
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }

            if ((inDto.getStateAction() == StateAction.CANCEL_REVIEW || inDto.getStateAction() == StateAction.REJECT_EVENT)
                    && event.getState() != State.PENDING) {
                String message = String.format("Мероприятие с id %d нельзя отменить. Текущий статус: %s", eventId, event.getState());
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
        }
        Category category = null;
        if (inDto.getCategory() != null) {
            category = getCategoryById(inDto.getCategory());
        }
        Event newestEntity = EventMapping.mapToAdminUpdateEntity(event, inDto, category);

        Event result = eventStorage.update(newestEntity);
        return viewsSupplerForFullDto(List.of(result)).stream().findFirst().orElseThrow(() -> {
            String message = "Не была получена статистика.";
            log.warn(message);
            return new RequestException(message, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка получения статистики.");
        });
    }

    @Override
    @Transactional
    public List<EventOutShortDto> getAllForPublic(EventPublicSearchParams params) {
        List<Event> events = eventStorage.findAllForPublic(params);
        if (params.isOnlyAvailable()) {
            events = events.stream().filter(e -> {
                long count = e.getRequests().stream().filter(es -> es.getStatus() == Status.CONFIRMED).count();
                return e.getParticipantLimit() > count;
            }).collect(Collectors.toList());
        }
        List<EventOutShortDto> result = viewsSupplerForShortDto(events);
        if (params.getSort() != null) {
            switch (params.getSort()) {
                case VIEWS:
                    result = result.stream().sorted((o1, o2) -> (int) (o1.getViews() - o2.getViews())).collect(Collectors.toList());
                    break;
                case EVENT_DATE:
                    result = result.stream()
                            .sorted(Comparator.comparing(o -> LocalDateTime.parse(o.getEventDate())))
                            .collect(Collectors.toList());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public EventOutFullDto getEventByEventIdForPublic(long eventId) {
        Event event = getEventById(eventId);
        if (event.getState() != State.PUBLISHED) {
            String message = String.format("Мероприятие с id %d не опубликовано.", eventId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
        }
        return viewsSupplerForFullDto(List.of(event)).stream().findFirst().orElseThrow(() -> {
            String message = "Не была получена статистика.";
            log.warn(message);
            return new RequestException(message, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка получения статистики.");
        });
    }

    private List<EventOutShortDto> viewsSupplerForShortDto(List<Event> events) {
        if (!events.isEmpty()) {
            LocalDateTime start = events.stream().min(Comparator.comparing(Event::getCreated)).get().getCreated();
            List<String> urls = events.stream().map(e -> String.format("/events/%d", e.getId())).collect(Collectors.toList());
            List<StatsOutDto> allViews = client.getStats(StatParam.builder()
                    .start(start)
                    .end(LocalDateTime.now())
                    .uniqueIp(true)
                    .requestUris(urls)
                    .build());
            return events.stream().map(e -> {
                long v = 0;
                for (StatsOutDto view : allViews) {
                    if (view.getUri().equals(String.format("/events/%d", e.getId()))) {
                        v = view.getHits();
                        break;
                    }
                }
                return EventMapping.mapToShortDto(e, v);
            }).collect(Collectors.toList());
        }
        return List.of();
    }

    private List<EventOutFullDto> viewsSupplerForFullDto(List<Event> events) {
        if (!events.isEmpty()) {
            LocalDateTime start = events.stream()
                    .min(Comparator.comparing(event -> event.getPublishedOn() == null ? event.getCreated() : event.getPublishedOn()))
                    .get()
                    .getCreated();
            List<String> urls = events.stream().map(e -> String.format("/events/%d", e.getId())).collect(Collectors.toList());
            List<StatsOutDto> allViews = client.getStats(StatParam.builder()
                    .start(start)
                    .end(LocalDateTime.now())
                    .uniqueIp(true)
                    .requestUris(urls)
                    .build());
            return events.stream().map(e -> {
                long v = 0;
                for (StatsOutDto view : allViews) {
                    if (view.getUri().equals(String.format("/events/%d", e.getId()))) {
                        v = view.getHits();
                        break;
                    }
                }
                return EventMapping.mapToFullDto(e, v);
            }).collect(Collectors.toList());
        }
        return List.of();
    }

    private void changeRequestStatus(List<Request> requests, long remained, Status statusInDto) {
        for (Request request : requests) {
            if (request.getStatus() == statusInDto) {
                String message = String.format("Статус заявки на мероприятие нельзя назначить повторно. " +
                        "Статус заявки и статус в запросе: %s | %s.", request.getStatus(), statusInDto);
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            if ((statusInDto == Status.REJECTED || statusInDto == Status.CANCELED) && request.getStatus() == Status.CONFIRMED) {
                String message = "Нельзя отменить уже принятую заявку на участие.";
                log.warn(message);
                throw new RequestException(message, HttpStatus.CONFLICT, errorRequest);
            }
            if (statusInDto == Status.CONFIRMED) {


                if (remained > 0) {
                    request.setStatus(statusInDto);
                    remained--;
                } else {
                    request.setStatus(Status.REJECTED);
                }
            } else {
                request.setStatus(statusInDto);
            }
        }
    }

    private void isOwnerEvent(long userId, Event event) {
        if (event.getInitiator().getId() != userId) {
            String message = String.format("Пользователь с id %d не создавал мероприятие с id %d.", userId, event.getId());
            log.warn(message);
            throw new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
        }
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

    private Category getCategoryById(long catId) {
        return categoryStorage.findById(catId).orElseThrow(() -> {
                    String message = String.format("Категория с id %d не найдена.", catId);
                    log.warn(message);
                    return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
                }
        );
    }

    private Long getViews(Event event) {
        StatParam param = StatParam.builder()
                .start(event.getCreated())
                .end(LocalDateTime.now())
                .uniqueIp(false)
                .requestUris(List.of("/events/" + event.getId()))
                .build();
        return client.getStats(param).stream().findFirst().orElseThrow(() -> {
            String message = String.format("Не была получена статистика по urls : %s.", param.getRequestUris());
            log.warn(message);
            return new RequestException(message, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка получения статистики.");
        }).getHits();
    }


}
