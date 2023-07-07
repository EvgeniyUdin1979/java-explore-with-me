package ru.practicum.client;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.StatsDto;

import java.util.List;

@Service
public class StatsClient implements BaseClient {
    private final String serverUrl = "http://localhost:9090/hit";

    @Override
    public HttpStatus sendStats(SendParams params) {
        RestTemplate restTemplate = new RestTemplate();
        StatsDto body = StatsDto.builder()
                .ip(params.getIp())
                .uri(params.getUri())
                .app(params.getApp())
                .timestamp(params.getCreated())
                .build();
        HttpEntity<StatsDto> requestEntity = new HttpEntity<>(body, defaultHeaders());
        ResponseEntity<Object> exchange = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, Object.class);
        return exchange.getStatusCode();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
