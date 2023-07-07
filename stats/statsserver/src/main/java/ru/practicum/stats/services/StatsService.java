package ru.practicum.stats.services;

import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.model.ParamGet;

import java.util.List;

public interface StatsService {
    StatsDto add(StatsDto stats);

    List<StatsOutDto> findAll(ParamGet params);
}
