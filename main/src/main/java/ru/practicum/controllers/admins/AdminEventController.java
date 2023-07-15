package ru.practicum.controllers.admins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventOutFullDto;
import ru.practicum.events.dto.EventUpdateAdminInDto;
import ru.practicum.events.model.EventAdminSearchParams;
import ru.practicum.events.model.State;
import ru.practicum.events.service.EventService;

import javax.validation.Valid;
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
public class AdminEventController {

    private final EventService service;

    @Autowired
    public AdminEventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventOutFullDto> getAllForAdmin(
            @RequestParam("users") Optional<List<Long>> users,
            @RequestParam("states") Optional<List<Optional<String>>> states,
            @RequestParam("categories") Optional<List<Long>> categories,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam("rangeStart") LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam("rangeEnd") LocalDateTime rangeEnd,
            @PositiveOrZero(message = "{validation.fromPositiveOrZero}")
            @RequestParam("from") int from,
            @Positive(message = "{validation.sizePositive}")
            @RequestParam("from") int size) {
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
        List<EventOutFullDto> result = service.getAllForAdmin(params);
        log.info("Получен список событий для админа {}", result);
        return result;
    }

    @PatchMapping("/{eventId}")
    private EventOutFullDto updateEventByAdmin(
            @Valid @RequestBody EventUpdateAdminInDto inDto,
            @Positive(message = "validation.eventIdPositive")
            @PathVariable("eventId") long eventId){
        EventOutFullDto result =  service.updateEventByAdmin(inDto, eventId);
        log.info("Админ обновил мероприятие с id {} : {}", eventId, result);
        return result;

    }
}
