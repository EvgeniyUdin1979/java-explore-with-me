package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryInDto;
import ru.practicum.categories.dto.CategoryOutDto;

import java.util.List;

public interface CategoryService {
    CategoryOutDto add(CategoryInDto inDto);

    CategoryOutDto update(CategoryInDto inDto, long catId);

    void deleteById(long catId);

    List<CategoryOutDto> findAll(int from, int size);

    CategoryOutDto findById(int catId);
}
