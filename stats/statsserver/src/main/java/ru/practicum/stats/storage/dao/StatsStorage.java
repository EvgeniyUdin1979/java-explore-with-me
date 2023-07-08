package ru.practicum.stats.storage.dao;

import ru.practicum.dto.StatParam;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.stats.model.Stats;

import java.util.List;

public interface StatsStorage {
    Stats add(Stats stats);

    List<StatsOutDto> get(StatParam param);


}
