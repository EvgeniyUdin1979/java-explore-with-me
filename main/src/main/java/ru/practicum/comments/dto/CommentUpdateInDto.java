package ru.practicum.comments.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.comments.model.CommentStatus;

@Data
@NoArgsConstructor
public class CommentUpdateInDto {

    @Length(min = 1, max = 7000, message = "{validation.textUpdateLength}")
    private String text;

    private CommentStatus status;
}
