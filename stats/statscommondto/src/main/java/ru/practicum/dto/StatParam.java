package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class StatParam {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> requestUris;
    private boolean uniqueIp;
}
