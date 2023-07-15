package ru.practicum.events.service;

import ru.practicum.events.dto.*;
import ru.practicum.events.model.EventAdminSearchParams;
import ru.practicum.events.model.EventPublicSearchParams;
import ru.practicum.request.dto.ParticipationRequestOutDto;

import java.util.List;

public interface EventService {
    EventOutShortDto add(EventInDto inDto, long userId);

    EventOutFullDto update(EventUpdateInDto inDto, long userid, long eventId);

    List<EventOutShortDto> getAllByUserId(long userid, int from, int size);

    EventOutFullDto getByUserId(long userid, long eventId);

    List<ParticipationRequestOutDto> getRequestsInEventByUserId(long userid, long eventId);

    List<EventOutFullDto> getAllForAdmin(EventAdminSearchParams params);

    EventOutFullDto updateEventByAdmin(EventUpdateAdminInDto inDto, long eventId);

    List<EventOutShortDto> getAllForPublic(EventPublicSearchParams params);

    EventOutFullDto getEventByEventIdForPublic(long eventId);

    EventRequestsStatusUpdateOutDto updateRequestsInEventByUserId(EventRequestsStatusUpdateInDto inDto, long userid, long eventId);
}
