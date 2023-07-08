package ru.practicum.stats.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatParam;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.stats.exceptions.RequestException;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.storage.dao.StatsStorage;
import ru.practicum.stats.util.StatsMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsStorage storage;

    @Autowired
    public StatsServiceImpl(StatsStorage storage) {
        this.storage = storage;
    }

    @Override
    public StatsDto add(StatsDto dto) {
        Stats stats = StatsMapper.mapToStats(dto);
        Stats result = storage.add(stats);
        return StatsMapper.mapToDto(result);
    }

    @Override
    public List<StatsOutDto> findAll(StatParam params) {
        LocalDateTime start = params.getStart();
        LocalDateTime end = params.getEnd();
        if (start.isAfter(LocalDateTime.now()) || end.isBefore(start)) {
            String message = "Дата в запросе указана не верно";
            log.warn(message);
            throw new RequestException(message);
        }
        return storage.get(params);
    }
}
