package ru.practicum.compilations.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationInDto;
import ru.practicum.compilations.dto.CompilationOutDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.storage.CompilationStorageDao;
import ru.practicum.compilations.util.CompilationMapping;
import ru.practicum.events.dto.EventOutShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.ViewsSuppler;
import ru.practicum.events.storage.EventStorageDao;
import ru.practicum.exceptions.RequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final String errorRequest = "Запрос содержит не корректные данные";

    private final CompilationStorageDao compilationStorage;

    private final EventStorageDao eventStorage;
    private final ViewsSuppler suppler;

    public CompilationServiceImpl(CompilationStorageDao compilationStorage, EventStorageDao eventStorage, ViewsSuppler suppler) {
        this.compilationStorage = compilationStorage;
        this.eventStorage = eventStorage;
        this.suppler = suppler;
    }


    @Override
    @Transactional
    public CompilationOutDto addForAdmin(CompilationInDto inDto) {
        List<Event> events = List.of();
        if (inDto.getEvents() != null) {
            events = eventStorage.findAllByIds(inDto.getEvents());
        }
        Compilation compilation = CompilationMapping.mapToEntity(inDto, events);
        Compilation result = compilationStorage.add(compilation);
        List<EventOutShortDto> eventsOutDto = suppler.viewsSupplerForShortDto(result.getEvents());
        return CompilationMapping.mapToOutDto(result, eventsOutDto);
    }

    @Override
    @Transactional
    public CompilationOutDto updateByIdForAdmin(CompilationInDto inDto, long compId) {
        Compilation old = getCompilationById(compId);
        if (inDto.getEvents() != null) {
            old.setEvents(new ArrayList<>(eventStorage.findAllByIds(inDto.getEvents())));
        }
        if (inDto.getPinned() != null) {
            old.setPinned(inDto.getPinned());
        }
        if (inDto.getTitle() != null) {
            old.setTitle(inDto.getTitle());
        }
        Compilation result = compilationStorage.update(old);
        List<EventOutShortDto> eventsOutDto = suppler.viewsSupplerForShortDto(result.getEvents());
        return CompilationMapping.mapToOutDto(result, eventsOutDto);
    }

    @Override
    @Transactional
    public void deleteByIdForAdmin(long compId) {
        try {
            compilationStorage.deleteById(compId);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Подборка событий с id %d не найдена.", compId);
            log.warn(message);
            throw new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
        }
    }

    @Override
    @Transactional
    public List<CompilationOutDto> getAllForPublic(Optional<Boolean> pinned, int from, int size) {
        List<Compilation> compilations = compilationStorage.findAll(pinned, from, size);
        return compilations.stream().map(c ->
                CompilationMapping.mapToOutDto(c, suppler.viewsSupplerForShortDto(c.getEvents()))
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationOutDto findById(long compId) {
        Compilation compilation = getCompilationById(compId);
        List<EventOutShortDto> events = suppler.viewsSupplerForShortDto(compilation.getEvents());
        return CompilationMapping.mapToOutDto(compilation, events);
    }

    private Compilation getCompilationById(long compId) {
        return compilationStorage.findById(compId).orElseThrow(() -> {
                    String message = String.format("Подборка событий с id %d не найдена.", compId);
                    log.warn(message);
                    return new RequestException(message, HttpStatus.NOT_FOUND, errorRequest);
                }
        );
    }
}
