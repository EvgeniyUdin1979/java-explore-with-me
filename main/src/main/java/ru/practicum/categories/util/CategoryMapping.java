package ru.practicum.categories.util;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.dto.CategoryInDto;
import ru.practicum.categories.dto.CategoryOutDto;
import ru.practicum.categories.model.Category;

@UtilityClass
public class CategoryMapping {

    public Category mapToEntity(CategoryInDto inDto){
        return Category.builder()
                .name(inDto.getName())
                .build();
    }

    public CategoryOutDto mapToOut(Category category){
        return new CategoryOutDto(category.getId(), category.getName());
    }
}
