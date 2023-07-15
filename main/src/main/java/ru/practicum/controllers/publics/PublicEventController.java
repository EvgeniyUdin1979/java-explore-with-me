package ru.practicum.controllers.publics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventOutFullDto;
import ru.practicum.events.dto.EventOutShortDto;
import ru.practicum.events.model.EventPublicSearchParams;
import ru.practicum.events.model.Sort;
import ru.practicum.events.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/events")
public class PublicEventController {

    private final EventService service;

    @Autowired
    public PublicEventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventOutShortDto> getAllForPublic(
            @RequestParam("text") Optional<String> text,
            @RequestParam("categories") Optional<List<Long>> categories,
            @RequestParam("paid") Optional<Boolean> paid,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam("rangeStart") LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam("rangeEnd") LocalDateTime rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
            @RequestParam("sort") Optional<String> sort,
            @PositiveOrZero(message = "{validation.fromPositiveOrZero}")
            @RequestParam(value = "from", defaultValue = "0") int from,
            @Positive(message = "{validation.sizePositive}")
            @RequestParam(value = "from", defaultValue = "0") int size) {

        List<Long> categoryIdLIst = new ArrayList<>(categories.orElse(List.of()));
        Sort s = Sort.from(sort.orElse(null)).orElse(null);
        EventPublicSearchParams params = EventPublicSearchParams.builder()
                .text(text.orElse(null))
                .categories(categoryIdLIst)
                .paid(paid.orElse(null))
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(s)
                .from(from)
                .size(size)
                .build();

        List<EventOutShortDto> result = service.getAllForPublic(params);
        log.info("Получен список событий для паблика {}", result);
        return result;
    }

    @GetMapping("/{eventId}")
    private EventOutFullDto getEventByEventIdForPublic(
            @Positive(message = "validation.eventIdPositive")
            @PathVariable("eventId") long eventId) {
        EventOutFullDto result = service.getEventByEventIdForPublic(eventId);
        log.info("Получено мероприятие {}", result);
        return result;

    }

}
