package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.StateAction;
import ru.practicum.events.validate.EventDateConstraint;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventUpdateAdminInDto {

    @Length(min = 20, max = 2000, message = "validation.annotationLength")
    private String annotation;

    @Length(min = 20, max = 7000, message = "validation.descriptionLength")
    private String description;

    private Integer category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EventDateConstraint
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    StateAction stateAction;

    @Length(min = 3, max = 120, message = "validation.titleLength")
    private String title;

}
