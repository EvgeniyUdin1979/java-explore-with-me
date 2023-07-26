package ru.practicum.categories.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CategoryInDto {

    @NotBlank(message = "Наименование категории не может быть пустым или отсутствовать")
    @Length(min = 1, max = 50, message = "Название категории не может быть меньше 1 и больше 50 символов.")
    private String name;
}
