package ru.practicum.stats.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class StatsOutDto {
    private String app;
    private String uri;
    private Long hits;


}
