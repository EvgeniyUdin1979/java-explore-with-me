package ru.practicum.categories.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryStorageImpl implements CategoryStorageDao {

    private final CategoryRepository repository;

    @Autowired
    public CategoryStorageImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category add(Category category) {
        return repository.save(category);
    }

    @Override
    public Optional<Category> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Category> findAll(int from, int size) {
        return repository.findAll(PageRequest.of(from, size)).toList();
    }
}
