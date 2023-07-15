package ru.practicum.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.Location;
import ru.practicum.events.validate.EventDateConstraint;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventInDto {
    @Length(min = 20, max = 2000, message = "Примечание не может быть меньше 20 и больше 2000 символов.")
    @NotBlank(message = "Примечание не может отсутствовать или быть пустым.")
    private String annotation;

    private long category;

    @NotBlank(message = "Описание не может отсутствовать или быть пустым.")
    @Length(min = 20, max = 7000, message = "Описание не может быть меньше 20 и больше 7000 символов.")
    private String description;

    @EventDateConstraint
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "Заголовок не может отсутствовать или быть пустым.")
    @Length(min = 3, max = 120, message = "Примечание не может быть меньше 3 и больше 120 символов.")
    private String title;

}
