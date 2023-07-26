package ru.practicum.categories.storage;

import ru.practicum.categories.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryStorageDao {
    Category add(Category category);

    Optional<Category> findById(long id);

    void delete(long id);

    List<Category> findAll(int from, int size);
}
