package ru.practicum.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.Status;

import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestsStatusUpdateInDto {
    private List<Long>  requestIds;

    private Status status;

}
