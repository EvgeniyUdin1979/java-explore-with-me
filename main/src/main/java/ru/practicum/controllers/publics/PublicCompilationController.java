package ru.practicum.controllers.publics;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationOutDto;
import ru.practicum.compilations.service.CompilationService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/compilations")
@Hidden
public class PublicCompilationController {
    private final CompilationService service;

    @Autowired
    public PublicCompilationController(CompilationService service) {
        this.service = service;
    }

    @GetMapping
    public List<CompilationOutDto> getAllCompilationForPublic(
            @RequestParam(value = "pinned") Optional<Boolean> pinned,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<CompilationOutDto> result = service.getAllForPublic(pinned, from, size);
        log.info("Получены подборки событий {}", result);
        return result;
    }

    @GetMapping("/{compId}")
    public CompilationOutDto getCompilationByIdForPublic(
            @PathVariable(value = "compId") long compId) {
        CompilationOutDto result = service.findById(compId);
        log.info("Получена подборка с id {}", compId);
        return result;

    }

}
