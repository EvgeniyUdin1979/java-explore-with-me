package ru.practicum.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.dto.ParticipationRequestOutDto;

import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestsStatusUpdateOutDto {
    private List<ParticipationRequestOutDto> confirmedRequests;
    private List<ParticipationRequestOutDto> rejectedRequests;
}
