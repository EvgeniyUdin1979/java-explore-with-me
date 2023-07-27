package ru.practicum.comments.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CommentInDto {

    @Length(max = 7000, message = "{validation.textLength}")
    @NotBlank(message = "{validation.textNotBlank}")
    private String text;

}
