package ru.practicum.controllers.admins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationInDto;
import ru.practicum.compilations.dto.CompilationOutDto;
import ru.practicum.compilations.service.CompilationService;
import ru.practicum.compilations.valid.Create;
import ru.practicum.compilations.valid.Update;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final CompilationService service;

    @Autowired
    public AdminCompilationController(CompilationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationOutDto addCompilationForAdmin(@Validated(value = Create.class) @RequestBody CompilationInDto inDto) {
        CompilationOutDto result = service.addForAdmin(inDto);
        log.info("Добавлена подборка событий: {}", result);
        return result;
    }

    @PatchMapping("/{compId}")
    public CompilationOutDto updateCompilationByIdForAdmin(
            @Validated(value = Update.class) @RequestBody CompilationInDto inDto,
            @PathVariable(value = "compId") long compId) {
        CompilationOutDto result = service.updateByIdForAdmin(inDto, compId);
        log.info("Обновлена подборка событий: {}", result);
        return result;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationByIdForAdmin(@PathVariable(value = "compId") long compId) {
        service.deleteByIdForAdmin(compId);
        log.info("Удалена подборка с id {}", compId);
    }

}
