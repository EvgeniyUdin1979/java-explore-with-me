package ru.practicum.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


class StatsClientTest {

    BaseClient client = new StatsClient();

    @Test
    void sendStats() {
        SendParams params = SendParams.builder()
                .app("main-test")
                .uri("/test/1")
                .ip("192.168.1.1")
                .build();
        HttpStatus httpStatus = client.sendStats(params);
        System.out.println(httpStatus);
    }
}