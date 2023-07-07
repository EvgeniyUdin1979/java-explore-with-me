package ru.practicum.stats.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.model.ParamGet;
import ru.practicum.stats.services.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class StatsController {

    private final StatsService service;

    public StatsController(StatsService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsDto addStats(@Valid @RequestBody StatsDto dto) {
        StatsDto result = service.add(dto);
        log.info("Добавлена статистика {}", result);
        return result;
    }

    @GetMapping("/stats")
    public List<StatsOutDto> getStats(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam LocalDateTime start,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam LocalDateTime end,
            @RequestParam Optional<List<String>> uris,
            @RequestParam(defaultValue = "false") Optional<Boolean> unique,
            HttpServletRequest request) {
        log.debug("request {}", request);
        ParamGet params = ParamGet.builder()
                .start(start)
                .end(end)
                .requestUris(uris.orElse(List.of()))
                .uniqueIp(unique.orElse(false))
                .build();
        log.info("Запрос на статистику {}", params);
        List<StatsOutDto> result = service.findAll(params);
        log.info("Получена статистика: {}", result);
        return result;
    }
}
