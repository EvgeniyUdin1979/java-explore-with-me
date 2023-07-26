package ru.practicum.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserInDto {
    @Email(message = "Электронный адрес не корректный.")
    @NotBlank(message = "Электронный адрес не может отсутствовать или быть пустым.")
    @Length(min = 6, max = 254, message = "Электронный адрес не может быть меньше 6 и больше 254 символов.")
    private String email;

    @NotBlank(message = "Имя не может отсутствовать или быть пустым.")
    @Length(min = 2, max = 250, message = "Имя не может быть меньше 2 и больше 250 символов.")
    private String name;
}
