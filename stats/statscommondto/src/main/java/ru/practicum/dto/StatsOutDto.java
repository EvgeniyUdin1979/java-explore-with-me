package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class StatsOutDto {
    private String app;
    private String uri;
    private Long hits;


}
