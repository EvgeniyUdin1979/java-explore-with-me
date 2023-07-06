package ru.practicum.stats.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.stats.StatsServer;
import ru.practicum.stats.dto.StatsOutDto;
import ru.practicum.stats.model.ParamGet;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.storage.dao.StatsStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//@SpringBootTest(classes = StatsServer.class)
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
        List<StatsOutDto> result1 = storage.get(paramGet);
        assertThat(result1, hasSize(1));
        assertThat(result1.get(0), hasProperty("id", is(3L)));
        assertThat(result1.get(0), hasProperty("addressIp", is("192.168.0.2")));
        stats.setAddressIp("192.168.1.1");
        List<StatsOutDto> result2 = storage.get(paramGet);
        assertThat(result2, hasSize(1));
        assertThat(result2.get(0), hasProperty("id", is(3L)));
        assertThat(result2.get(0), hasProperty("addressIp", is("192.168.1.1")));
    }

    @Test
    void getDistinctByAddressIpAndRequestTimeBetween() {
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .uniqueIp(true)
                .build();
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(1));
        assertThat(result.get(0), hasProperty("id", is(1L)));
        assertThat(result.get(0), hasProperty("addressIp", is("192.168.0.1")));
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
        assertThat(result.get(1), hasProperty("id", is(2L)));
        assertThat(result.get(1), hasProperty("requestUrl", is("/events/2")));
    }

    @Test
    void getDistinctByAddressIpAndRequestTimeBetweenAndRequestUrl() {
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .requestUris(List.of("/events/2", "/events"))
                .uniqueIp(true)
                .build();
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(1));
        assertThat(result.get(0), hasProperty("id", is(2L)));
        assertThat(result.get(0), hasProperty("requestUrl", is("/events/2")));
    }

    @Test
    void getRequestTimeBetweenAndRequestUrl() {
        ParamGet paramGet = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .requestUris(List.of("/events"))
                .uniqueIp(false)
                .build();
        List<StatsOutDto> result = storage.get(paramGet);
        assertThat(result, hasSize(1));
        assertThat(result.get(0), hasProperty("id", is(2L)));
        assertThat(result.get(0), hasProperty("requestUrl", is("/events/2")));
    }
}