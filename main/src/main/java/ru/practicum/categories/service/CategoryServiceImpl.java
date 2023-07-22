package ru.practicum.categories.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryInDto;
import ru.practicum.categories.dto.CategoryOutDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.storage.CategoryStorageDao;
import ru.practicum.categories.util.CategoryMapping;
import ru.practicum.exceptions.RequestException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryStorageDao storage;

    @Autowired
    public CategoryServiceImpl(CategoryStorageDao storage) {
        this.storage = storage;
    }

    @Override
    public CategoryOutDto add(CategoryInDto inDto) {
        Category category = CategoryMapping.mapToEntity(inDto);
        Category result = storage.add(category);
        return CategoryMapping.mapToOut(result);
    }

    @Override
    public CategoryOutDto update(CategoryInDto inDto, final long catId) {
        Category result = storage.findById(catId)
                .orElseThrow(() -> new RequestException(
                        String.format("Категория с id %d не найдена.", catId),
                        HttpStatus.NOT_FOUND, "Ошибка при создании категории."));
        result.setName(inDto.getName());
        storage.add(result);
        return CategoryMapping.mapToOut(result);
    }

    @Override
    public void deleteById(final long catId) {
        String reason = "Ошибка при удалении категории.";
        Category result = storage.findById(catId)
                .orElseThrow(() -> new RequestException(
                        String.format("Категория с id %d не найдена.", catId),
                        HttpStatus.NOT_FOUND, reason));
        if (!result.getEvents().isEmpty()) {
            String message = String.format("Категория id %d содержит элементы и не доступна для изменения", catId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.CONFLICT, reason);
        }
        storage.delete(catId);
    }

    @Override
    public List<CategoryOutDto> findAll(int from, int size) {
        List<Category> categories = storage.findAll(from, size);
        return categories.stream().map(CategoryMapping::mapToOut).collect(Collectors.toList());
    }

    @Override
    public CategoryOutDto findById(int catId) {
        Category category = storage.findById(catId).orElseThrow(() -> {
            String message = String.format("Категория с id %d не найдена.", catId);
            log.warn(message);
            return new RequestException(message, HttpStatus.NOT_FOUND, "Запрос содержит не корректные данные");
        });
        return CategoryMapping.mapToOut(category);
    }

}
