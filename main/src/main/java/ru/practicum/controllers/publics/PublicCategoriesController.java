package ru.practicum.controllers.publics;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryOutDto;
import ru.practicum.categories.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
@Validated
@Hidden
public class PublicCategoriesController {

    private final CategoryService service;

    @Autowired
    public PublicCategoriesController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryOutDto> getAllCategories(
            @PositiveOrZero(message = "{validation.fromPositiveOrZero}")
            @RequestParam(value = "from", defaultValue = "0") int from,
            @Positive(message = "{validation.sizePositive}")
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<CategoryOutDto> result = service.findAll(from, size);
        log.info("Получены категории {}", result);
        return result;
    }

    @GetMapping("{catId}")
    public CategoryOutDto getCategoryById(
            @Positive(message = "{validation.catIdPositive}")
            @PathVariable("catId") int catId) {
        CategoryOutDto result = service.findById(catId);
        log.info("Получена категория {}", result);
        return result;
    }



}
