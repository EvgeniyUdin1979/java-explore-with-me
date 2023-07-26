package ru.practicum.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.Location;
import ru.practicum.events.validate.EventDatePrivateConstraint;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventInDto {
    @Length(min = 20, max = 2000, message = "{validation.annotationLength}")
    @NotBlank(message = "{validation.annotationNotBlank}")
    private String annotation;

    private long category;

    @NotBlank(message = "{validation.descriptionNotBlank}")
    @Length(min = 20, max = 7000, message = "{validation.descriptionLength}")
    private String description;

    @EventDatePrivateConstraint
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "{validation.titleNotBlank}")
    @Length(min = 3, max = 120, message = "{validation.titleLength}")
    private String title;

}
