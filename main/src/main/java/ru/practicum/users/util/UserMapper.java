package ru.practicum.users.util;

import lombok.experimental.UtilityClass;
import ru.practicum.users.dto.UserInDto;
import ru.practicum.users.dto.UserOutDto;
import ru.practicum.users.dto.UserOutShortDto;
import ru.practicum.users.model.User;

@UtilityClass
public class UserMapper {
    public User mapToEntity(UserInDto inDto) {
        return User.builder()
                .email(inDto.getEmail())
                .name(inDto.getName())
                .build();
    }

    public UserOutDto mapToOut(User user) {
        return UserOutDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserOutShortDto mapToOutShort(User user) {
        return UserOutShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
