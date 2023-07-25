package ru.practicum.compilations.util;

import lombok.experimental.UtilityClass;
import ru.practicum.compilations.dto.CompilationInDto;
import ru.practicum.compilations.dto.CompilationOutDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventOutShortDto;
import ru.practicum.events.model.Event;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CompilationMapping {
    public Compilation mapToEntity(CompilationInDto inDto, List<Event> events) {
        Compilation.CompilationBuilder builder = Compilation.builder();
        if (events != null) {
            builder.events(new ArrayList<>(events));
        }
        if (inDto.getTitle() != null) {
            builder.title(inDto.getTitle());
        }
        if (inDto.getPinned() != null) {
            builder.pinned(inDto.getPinned());
        }
        return builder.build();
    }

    public static CompilationOutDto mapToOutDto(Compilation compilation, List<EventOutShortDto> events) {
        return CompilationOutDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }
}
