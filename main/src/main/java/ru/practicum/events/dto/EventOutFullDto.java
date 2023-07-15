package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoryOutDto;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.users.dto.UserOutShortDto;

@Data
@Builder
public class EventOutFullDto {

    private long id;

    private String annotation;

    private CategoryOutDto category;

    private long confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private UserOutShortDto initiator;

    private Location location;

    private Boolean paid;

    private int participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private State state;

    private String title;

    private long views;
}
