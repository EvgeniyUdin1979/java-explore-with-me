package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.events.dto.EventOutShortDto;

import java.util.List;

@Data
@Builder
public class CompilationOutDto {
    private long id;
    private List<EventOutShortDto>  events;
    private boolean pinned;
    private String title;
}
