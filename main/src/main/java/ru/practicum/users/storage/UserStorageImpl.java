package ru.practicum.users.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserStorageImpl implements UserStorageDao {

    private final UserRepository repository;

    @Autowired
    public UserStorageImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User add(User user) {
        return repository.save(user);
    }

    @Override
    public Long delete(long id) throws EmptyResultDataAccessException {
        return repository.deleteById(id);
    }

    @Override
    public List<User> findAllByIdIn(List<Long> ids, int from, int size) {
        return repository.findAllByIdIn(ids, PageRequest.of(from, size)).toList();
    }

    @Override
    public List<User> findAll(int from, int size) {
        return repository.findAll(PageRequest.of(from, size)).toList();
    }


    @Override
    public Optional<User> findById(long id) {
        return repository.findById(id);
    }
}
