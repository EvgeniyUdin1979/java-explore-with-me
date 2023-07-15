package ru.practicum.categories.storage;

import ru.practicum.categories.model.Category;

import java.util.Optional;

public interface CategoryStorageDao {
    Category add(Category category);

    Optional<Category> findDyId(long id);

    void delete(long id);
}
