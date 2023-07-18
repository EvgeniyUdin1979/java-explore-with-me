package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.request.dto.ParticipationRequestOutDto;

import java.util.List;

@Data
@Builder
public class EventRequestsStatusUpdateOutDto {
    private List<ParticipationRequestOutDto> confirmedRequests;
    private List<ParticipationRequestOutDto> rejectedRequests;
}
