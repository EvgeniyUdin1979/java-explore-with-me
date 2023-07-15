package ru.practicum.events.service;

import org.springframework.stereotype.Service;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.EventAdminSearchParams;
import ru.practicum.events.model.EventPublicSearchParams;
import ru.practicum.request.dto.ParticipationRequestOutDto;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public EventOutShortDto add(EventInDto inDto, long userId) {
        return null;
    }

    @Override
    public EventOutFullDto update(EventUpdateInDto inDto, long userid, long eventId) {
        return null;
    }

    @Override
    public List<EventOutShortDto> getAllByUserId(long userid, int from, int size) {
        return null;
    }

    @Override
    public EventOutFullDto getByUserId(long userid, long eventId) {
        return null;
    }

    @Override
    public List<ParticipationRequestOutDto> getRequestsInEventByUserId(long userid, long eventId) {
        return null;
    }

    @Override
    public List<EventOutFullDto> getAllForAdmin(EventAdminSearchParams params) {
        return null;
    }

    @Override
    public EventOutFullDto updateEventByAdmin(EventUpdateAdminInDto inDto, long eventId) {
        return null;
    }

    @Override
    public List<EventOutShortDto> getAllForPublic(EventPublicSearchParams params) {
        return null;
    }

    @Override
    public EventOutFullDto getEventByEventIdForPublic(long eventId) {
        return null;
    }

    @Override
    public EventRequestsStatusUpdateOutDto updateRequestsInEventByUserId(EventRequestsStatusUpdateInDto inDto, long userid, long eventId) {
        return null;
    }
}
