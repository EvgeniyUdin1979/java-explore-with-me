package ru.practicum.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserInDto {
    @Email( message = "Электронный адрес не корректный.")
    @NotNull(message = "Электронный адрес не может отсутствовать.")
    private String email;

    @NotBlank(message = "Имя не может отсутствовать или быть пустым.")
    @Length(min = 2, max = 250, message = "Имя не может быть меньше 2 и больше 250 символов.")
    private String name;
}
