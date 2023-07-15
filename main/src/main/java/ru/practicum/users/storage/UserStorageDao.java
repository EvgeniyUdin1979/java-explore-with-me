package ru.practicum.users.storage;

import ru.practicum.users.model.User;

import java.util.List;

public interface UserStorageDao {
    User add(User user);

    Long delete(long id);

    List<User> find(List<Long> ids, int from, int size);
}
