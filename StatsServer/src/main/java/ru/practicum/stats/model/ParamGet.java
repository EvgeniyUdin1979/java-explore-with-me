package ru.practicum.stats.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
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
