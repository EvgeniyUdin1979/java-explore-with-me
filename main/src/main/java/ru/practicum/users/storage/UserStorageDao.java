package ru.practicum.users.storage;

import ru.practicum.users.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorageDao {
    User add(User user);

    Long delete(long id);

    List<User> findAllByIdIn(List<Long> ids, int from, int size);

    List<User> findAll(int from, int size);

    Optional<User> findById(long id);
}
