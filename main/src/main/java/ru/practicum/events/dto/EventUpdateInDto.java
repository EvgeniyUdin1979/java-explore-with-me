package ru.practicum.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.StateAction;
import ru.practicum.events.validate.EventDatePrivateUpdateConstraint;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventUpdateInDto {
    @Length(min = 20, max = 2000, message = "{validation.annotationLength}")
    private String annotation;

    private Long category;

    @Length(min = 20, max = 7000, message = "{validation.descriptionLength}")
    private String description;

    @EventDatePrivateUpdateConstraint
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Length(min = 3, max = 120, message = "{validation.titleLength}")
    private String title;

}
