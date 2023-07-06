package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class StatsOutDto {
    private String app;
    private String uri;
    private Long hits;


}
