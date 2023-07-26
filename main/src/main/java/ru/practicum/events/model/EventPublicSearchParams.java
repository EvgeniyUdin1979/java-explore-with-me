package ru.practicum.events.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventPublicSearchParams {

    private String text;

    private List<Long> categories;

    private Boolean paid;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private boolean onlyAvailable;

    private Sort sort;

    private int from;

    private int size;
}
