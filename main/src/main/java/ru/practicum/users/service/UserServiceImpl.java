package ru.practicum.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.RequestException;
import ru.practicum.users.dto.UserInDto;
import ru.practicum.users.dto.UserOutDto;
import ru.practicum.users.model.User;
import ru.practicum.users.storage.UserStorageDao;
import ru.practicum.users.util.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorageDao storage;

    @Autowired
    public UserServiceImpl(UserStorageDao storage) {
        this.storage = storage;
    }

    @Override
    public UserOutDto add(UserInDto inDto) {
        User user = UserMapper.mapToEntity(inDto);
        User result = storage.add(user);
        return UserMapper.mapToOut(result);
    }

    @Override
    @Transactional
    public List<UserOutDto> get(Optional<List<Long>> ids, int from, int size) {
        return ids.map(longs -> storage.findAllByIdIn(longs, from, size).stream()
                        .map(UserMapper::mapToOut)
                        .collect(Collectors.toList()))
                .orElseGet(() -> storage.findAll(from, size).stream()
                        .map(UserMapper::mapToOut)
                        .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteById(long userId) {
        try {
            storage.delete(userId);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Пользователь с id %d не найден.", userId);
            throw new RequestException(message, HttpStatus.NOT_FOUND, "Ошибка при удалении пользователя.");
        }
    }
}
