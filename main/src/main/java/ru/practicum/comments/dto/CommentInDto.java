package ru.practicum.comments.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CommentInDto {

    @Length(max = 7000, message = "{validation.textLength}", groups = {Create.class, Update.class})
    @NotBlank(message = "{validation.textNotBlank}", groups = {Create.class})
    private String text;

}
