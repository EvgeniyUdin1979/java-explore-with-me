package ru.practicum.controllers.admins;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryInDto;
import ru.practicum.categories.dto.CategoryOutDto;
import ru.practicum.categories.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@Hidden
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryOutDto addCategory(@Valid
                                      @RequestBody CategoryInDto inDto) {
        CategoryOutDto result = categoryService.add(inDto);
        log.info("Создана категория {}", result);
        return result;
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryOutDto updateCategory(@Valid
                                         @RequestBody CategoryInDto inDto,
                                         @Positive(message = "validation.catIdPositive")
                                         @PathVariable(value = "catId") long catId) {
        CategoryOutDto result = categoryService.update(inDto, catId);
        log.info("Обновлено название категории {}", result);
        return result;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCategoryById(
            @Positive(message = "validation.catIdPositive")
            @PathVariable(value = "catId") long catId) {
        categoryService.deleteById(catId);
        log.info("Удалена категория id: {}", catId);
    }
}
