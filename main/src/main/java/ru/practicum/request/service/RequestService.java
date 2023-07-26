package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestOutDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestOutDto> findAllRequestsByUserId(long userId);

    ParticipationRequestOutDto addRequestByUserId(long userId, long eventId);

    ParticipationRequestOutDto patchRequestByUserId(long userId, long requestsId);
}
