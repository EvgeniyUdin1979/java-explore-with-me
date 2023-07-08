package ru.practicum.client;

import org.springframework.http.HttpStatus;
import ru.practicum.dto.StatParam;
import ru.practicum.dto.StatsOutDto;

import java.util.List;

public interface BaseClient {
    HttpStatus sendStats(SendParams params);

    List<StatsOutDto> getStats(StatParam param);
}
