package ru.practicum.events.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventAdminSearchParams {
    private List<Long> users;

    private List<State> states;

    private List<Long> categories;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private int from;

    private int size;
}
