package ru.practicum.stats.storage.dao;

import ru.practicum.stats.dto.StatsOutDto;
import ru.practicum.stats.model.ParamGet;
import ru.practicum.stats.model.Stats;

import java.util.List;

public interface StatsStorage {
    Stats add(Stats stats);

    List<StatsOutDto> get(ParamGet param);


}
