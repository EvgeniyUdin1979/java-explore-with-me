package ru.practicum.compilations.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.compilations.valid.Create;
import ru.practicum.compilations.valid.Update;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class CompilationInDto {
    private List<Long> events;

    private Boolean pinned;

    @NotBlank(message = "{validation.titleNotBlank}", groups = {Create.class})
    @Length(min = 1, max = 50, message = "{validation.titleCompilationLength}", groups = {Create.class, Update.class})
    private String title;
}
