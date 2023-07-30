package ru.practicum.controllers.admins;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventOutFullDto;
import ru.practicum.events.dto.EventUpdateAdminInDto;
import ru.practicum.events.model.EventAdminSearchParams;
import ru.practicum.events.model.State;
import ru.practicum.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/admin/events")
@Validated
@Hidden
public class AdminEventController {
    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventOutFullDto> getAllForAdmin(
            @RequestParam("users") Optional<List<Long>> users,
            @RequestParam("states") Optional<List<Optional<String>>> states,
            @RequestParam("categories") Optional<List<Long>> categories,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @PastOrPresent(message = "{validation.rangeStartPastOrPresent}")
            @RequestParam(value = "rangeStart", required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @Future(message = "{validation.rangeEndFuture}")
            @RequestParam(value = "rangeEnd", required = false) LocalDateTime rangeEnd,
            @PositiveOrZero(message = "{validation.fromPositiveOrZero}")
            @RequestParam(value = "from", defaultValue = "0") int from,
            @Positive(message = "{validation.sizePositive}")
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<State> stateList = states.orElse(List.of()).stream()
                .filter(Optional::isPresent)
                .map(s -> State.from(s.get()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        List<Long> userIdList = new ArrayList<>(users.orElse(List.of()));
        List<Long> categoryIdLIst = new ArrayList<>(categories.orElse(List.of()));

        EventAdminSearchParams params = EventAdminSearchParams.builder()
                .users(userIdList)
                .states(stateList)
                .categories(categoryIdLIst)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        List<EventOutFullDto> result = eventService.getAllForAdmin(params);
        log.info("Получен список событий для админа {}", result);
        return result;
    }

    @PatchMapping("/{eventId}")
    public EventOutFullDto updateEventByAdmin(
            @Valid @RequestBody EventUpdateAdminInDto inDto,
            @Positive(message = "validation.eventIdPositive")
            @PathVariable("eventId") long eventId) {
        EventOutFullDto result = eventService.updateEventByAdmin(inDto, eventId);
        log.info("Админ обновил мероприятие с id {} : {}", eventId, result);
        return result;
    }
}
