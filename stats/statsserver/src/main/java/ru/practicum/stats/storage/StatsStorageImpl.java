package ru.practicum.stats.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatParam;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.storage.dao.StatsJpaRepository;
import ru.practicum.stats.storage.dao.StatsStorage;

import java.util.List;

@Repository
public class StatsStorageImpl implements StatsStorage {

    private final StatsJpaRepository repository;

    @Autowired
    public StatsStorageImpl(StatsJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Stats add(Stats stats) {
        return repository.save(stats);
    }

    @Override
    public List<StatsOutDto> get(StatParam param) {
        if (param.isUniqueIp()) {
            if (param.getRequestUris() != null && !param.getRequestUris().isEmpty()) {
                return repository.findAllDistinctByAddressIpAndRequestTimeBetweenAndRequestUrl(
                        param.getStart(),
                        param.getEnd(),
                        param.getRequestUris());
            } else {
              return repository.findAllDistinctByAddressIpAndRequestTimeBetween(param.getStart(), param.getEnd());
            }
        } else {
            if (param.getRequestUris() != null && !param.getRequestUris().isEmpty()) {
                return repository.findAllByAddressIpAndRequestTimeBetweenAndRequestUrl(
                        param.getStart(),
                        param.getEnd(),
                        param.getRequestUris());
            } else {
                return repository.findAllByAddressIpAndRequestTimeBetween(param.getStart(), param.getEnd());
            }
        }
    }
}
