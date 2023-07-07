package ru.practicum.stats.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ParamGet {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> requestUris;
    private boolean uniqueIp;
}
