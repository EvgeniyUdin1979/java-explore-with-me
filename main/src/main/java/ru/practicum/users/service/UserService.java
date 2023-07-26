package ru.practicum.users.service;

import ru.practicum.users.dto.UserInDto;
import ru.practicum.users.dto.UserOutDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserOutDto add(UserInDto inDto);

    List<UserOutDto> get(Optional<List<Long>> ids, int from, int size);

    void deleteById(long userId);
}
