package ru.practicum.stats.util;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.StatsDto;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;

@UtilityClass
public class StatsMapper {
   public Stats mapToStats(StatsDto dto){
        return Stats.builder()
                .application(dto.getApp())
                .addressIp(dto.getIp())
                .requestUrl(dto.getUri())
                .requestTime(dto.getTimestamp())
                .build();
    }

    public StatsDto mapToDto(Stats stats){
       return StatsDto.builder()
               .app(stats.getApplication())
               .uri(stats.getRequestUrl())
               .ip(stats.getAddressIp())
               .timestamp(stats.getRequestTime())
               .build();
    }
}
