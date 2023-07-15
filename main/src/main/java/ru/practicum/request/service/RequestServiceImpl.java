package ru.practicum.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.request.dto.ParticipationRequestOutDto;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Override
    public List<ParticipationRequestOutDto> findAllRequestsByUserId(long userId) {
        return null;
    }

    @Override
    public ParticipationRequestOutDto addRequestByUserId(long userId, long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestOutDto patchRequestByUserId(long userId, long requestsId) {
        return null;
    }
}
