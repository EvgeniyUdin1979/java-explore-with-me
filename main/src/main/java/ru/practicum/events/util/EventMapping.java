package ru.practicum.events.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.util.CategoryMapping;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.model.StateAction;
import ru.practicum.exceptions.RequestException;
import ru.practicum.request.dto.ParticipationRequestOutDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.users.model.User;
import ru.practicum.users.util.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class EventMapping {
    private final String dateTameFormat = "yyyy-MM-dd HH:mm:ss";

    public static Event mapToEntity(EventInDto inDto, User user, Category category) {
        return Event.builder()
                .annotation(inDto.getAnnotation())
                .description(inDto.getDescription())
                .eventDate(inDto.getEventDate())
                .location(inDto.getLocation())
                .paid(inDto.getPaid() != null && inDto.getPaid())
                .participantLimit(inDto.getParticipantLimit() == null ? 0 : inDto.getParticipantLimit())
                .requestModeration(inDto.getRequestModeration() == null || inDto.getRequestModeration())
                .state(State.PENDING)
                .title(inDto.getTitle())
                .initiator(user)
                .category(category)
                .created(LocalDateTime.now())
                .build();
    }

    public static EventOutFullDto mapToFullDto(Event event, long views) {
        return EventOutFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapping.mapToOut(event.getCategory()))
                .confirmedRequests(event.getRequests() == null ? 0 : event.getRequests().stream().filter(r -> r.getStatus() == Status.CONFIRMED).count())
                .createdOn(event.getCreated().format(DateTimeFormatter.ofPattern(dateTameFormat)))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(dateTameFormat)))
                .id(event.getId())
                .initiator(UserMapper.mapToOutShort(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(DateTimeFormatter.ofPattern(dateTameFormat)))
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static Event mapToUpdateEntity(Event event, EventUpdateInDto updateInDto, Category cat) {
        if (updateInDto.getAnnotation() != null) {
            event.setAnnotation(updateInDto.getAnnotation());
        }
        if (cat != null) {
            event.setCategory(cat);
        }
        if (updateInDto.getDescription() != null) {
            event.setDescription(updateInDto.getDescription());
        }
        if (updateInDto.getEventDate() != null) {
            event.setEventDate(updateInDto.getEventDate());
        }
        if (updateInDto.getPaid() != null) {
            event.setPaid(updateInDto.getPaid());
        }
        if (updateInDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateInDto.getParticipantLimit());
        }
        if (updateInDto.getStateAction() != null) {
            StateAction action = updateInDto.getStateAction();
            event.setState(
                    State.valueOfStateAction(action).orElseThrow(() -> {
                        String message = String.format("Параметр запроса состояние : %s не корректно.", action);
                        log.warn(message);
                        return new RequestException(message, HttpStatus.BAD_REQUEST, "Запрос содержит не корректные данные");
                    }));
        }
        if (updateInDto.getTitle() != null) {
            event.setTitle(updateInDto.getTitle());
        }
        if (updateInDto.getLocation() != null) {
            event.setLocation(updateInDto.getLocation());
        }
        return event;
    }

    public static Event mapToAdminUpdateEntity(Event event, EventUpdateAdminInDto updateInDto, Category cat) {
        if (updateInDto.getAnnotation() != null) {
            event.setAnnotation(updateInDto.getAnnotation());
        }
        if (cat != null) {
            event.setCategory(cat);
        }
        if (updateInDto.getDescription() != null) {
            event.setDescription(updateInDto.getDescription());
        }
        if (updateInDto.getEventDate() != null) {
            event.setEventDate(updateInDto.getEventDate());
        }
        if (updateInDto.getPaid() != null) {
            event.setPaid(updateInDto.getPaid());
        }
        if (updateInDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateInDto.getParticipantLimit());
        }
        if (updateInDto.getStateAction() != null) {
            StateAction action = updateInDto.getStateAction();
            event.setState(
                    State.valueOfStateAction(action).orElseThrow(() -> {
                        String message = String.format("Параметр запроса состояние : %s не корректно.", action);
                        log.warn(message);
                        return new RequestException(message, HttpStatus.BAD_REQUEST, "Запрос содержит не корректные данные");
                    }));
            if (updateInDto.getStateAction() == StateAction.PUBLISH_EVENT) {
                event.setPublishedOn(LocalDateTime.now());
            }
        }
        if (updateInDto.getTitle() != null) {
            event.setTitle(updateInDto.getTitle());
        }
        if (updateInDto.getLocation() != null) {
            event.setLocation(updateInDto.getLocation());
        }
        return event;
    }

    public EventOutShortDto mapToShortDto(Event event, long views) {
        return EventOutShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapping.mapToOut(event.getCategory()))
                .confirmedRequests(event.getRequests().stream().filter(r -> r.getStatus() == Status.CONFIRMED).count())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(dateTameFormat)))
                .id(event.getId())
                .initiator(UserMapper.mapToOutShort(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static List<ParticipationRequestOutDto> mapToRequestsDto(List<Request> requests) {
        return requests.stream()
                .map(EventMapping::mapToRequestDto)
                .collect(Collectors.toList());

    }

    public static ParticipationRequestOutDto mapToRequestDto(Request request) {
        return ParticipationRequestOutDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated().format(DateTimeFormatter.ofPattern(dateTameFormat)))
                .build();
    }
}
