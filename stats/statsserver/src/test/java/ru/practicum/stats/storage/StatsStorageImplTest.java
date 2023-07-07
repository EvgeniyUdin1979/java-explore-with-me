package ru.practicum.stats.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.stats.dto.StatsOutDto;
import ru.practicum.stats.model.ParamGet;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.storage.dao.StatsStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@Sql(scripts = "file:src/test/resources/data/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/data/resetDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class StatsStorageImplTest {

    @Autowired
    StatsStorage storage;

    @Autowired
    private TestEntityManager em;

    @Test
    void add() {
        Stats stats = Stats.builder()
                .application("main")
                .addressIp("192.168.0.2")
                .requestUrl("main/1")
                .requestTime(LocalDateTime.now())
                .build();
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .requestUris(List.of("main/1"))
                .uniqueIp(false)
                .build();
        storage.add(stats);
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(1));
        assertThat(result.get(0), hasProperty("app", is("main")));
        assertThat(result.get(0), hasProperty("hits", is(1L)));

    }

    @Test
    void getDistinctByAddressIpAndRequestTimeBetween() {
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .uniqueIp(true)
                .build();
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(2));
        assertThat(result.get(0), hasProperty("uri", is("/events/1")));
        assertThat(result.get(0), hasProperty("hits", is(3L)));
        assertThat(result.get(1), hasProperty("uri", is("/events/2")));
        assertThat(result.get(1), hasProperty("hits", is(2L)));
    }

    @Test
    void getRequestTimeBetween() {
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .uniqueIp(false)
                .build();
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(2));
        assertThat(result.get(1), hasProperty("hits", is(3L)));
        assertThat(result.get(1), hasProperty("uri", is("/events/2")));
    }

    @Test
    void getDistinctByAddressIpAndRequestTimeBetweenAndRequestUrl() {
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .requestUris(List.of("/events/2", "/events/1"))
                .uniqueIp(true)
                .build();
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(2));
        assertThat(result.get(0), hasProperty("hits", is(3L)));
        assertThat(result.get(1), hasProperty("hits", is(2L)));
    }

    @Test
    void getRequestTimeBetweenAndRequestUrl() {
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .requestUris(List.of("/events/2"))
                .uniqueIp(false)
                .build();
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(1));
        assertThat(result.get(0), hasProperty("hits", is(3L)));
        assertThat(result.get(0), hasProperty("uri", is("/events/2")));
    }
}