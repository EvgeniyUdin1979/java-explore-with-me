package ru.practicum.stats.storage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.dto.StatsOutDto;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsJpaRepository extends JpaRepository<Stats, Long> {
    @Query(value =
            "select new ru.practicum.stats.dto.StatsOutDto(s.application, s.requestUrl, count( distinct s.addressIp) as c) " +
                    "from Stats s where s.requestTime between ?1 and ?2 " +
                    "group by s.application, s.requestUrl " +
                    "order by c desc")
    List<StatsOutDto> findAllDistinctByAddressIpAndRequestTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query(value =
            "select new ru.practicum.stats.dto.StatsOutDto(s.application, s.requestUrl, count( s.addressIp) as c) " +
                    "from Stats s where s.requestTime between ?1 and ?2 " +
                    "group by s.application, s.requestUrl " +
                    "order by c desc")
    List<StatsOutDto> findAllByAddressIpAndRequestTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query(value =
            "select new ru.practicum.stats.dto.StatsOutDto(s.application, s.requestUrl, count(distinct  s.addressIp) as c) " +
                    "from Stats s where s.requestTime between :start and :end and s.requestUrl in :uris " +
                    "group by s.application, s.requestUrl " +
                    "order by c desc")
    List<StatsOutDto> findAllDistinctByAddressIpAndRequestTimeBetweenAndRequestUrl(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value =
            "select new ru.practicum.stats.dto.StatsOutDto(s.application, s.requestUrl, count( s.addressIp) as c) " +
                    "from Stats s " +
                    "where s.requestTime between :start and :end and s.requestUrl in :uris  " +
                    "group by s.application, s.requestUrl " +
                    "order by c desc")
    List<StatsOutDto> findAllByAddressIpAndRequestTimeBetweenAndRequestUrl(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}
