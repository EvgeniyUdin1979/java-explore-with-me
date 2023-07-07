package ru.practicum.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.model.ParamGet;

import java.time.LocalDateTime;
import java.util.List;


class StatsClientTest {

    BaseClient client = new StatsClient();

    @Test
    void sendStats() {
        SendParams params = SendParams.builder()
                .app("main-test")
                .uri("/events/2")
                .ip("192.168.1.1")
                .build();
        HttpStatus httpStatus = client.sendStats(params);
        Assertions.assertEquals(httpStatus, HttpStatus.CREATED);
    }

    @Test
    void getStats() {
        ParamGet params = ParamGet.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .requestUris(List.of("/events/2", "/events/1"))
                .uniqueIp(false)
                .build();
        List<StatsOutDto> stats = client.getStats(params);
        Assertions.assertTrue(stats instanceof List);
    }
}