package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.StatParam;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.StatsOutDto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient implements BaseClient {
    private final String serverUrl;

    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public StatsClient(@Value("${stats.server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public HttpStatus sendStats(SendParams params) {

        StatsDto body = StatsDto.builder()
                .ip(params.getIp())
                .uri(params.getUri())
                .app(params.getApp())
                .timestamp(params.getCreated())
                .build();
        HttpEntity<StatsDto> requestEntity = new HttpEntity<>(body, defaultHeaders());
        ResponseEntity<Object> exchange = restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, Object.class);
        return exchange.getStatusCode();
    }

    @Override
    public List<StatsOutDto> getStats(StatParam params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(serverUrl + "/stats")
                .queryParam("start", "{start}")
                .queryParam("end", "{end}")
                .queryParam("unique", "{unique}");
        if (params.getRequestUris() != null && !params.getRequestUris().isEmpty()) {
            params.getRequestUris().forEach(p -> {
                builder.queryParam("uris", p);
            });
        }
        String uriString = builder
                .encode()
                .toUriString();

        Object response = restTemplate.getForObject(
                uriString,
                Object.class,
                Map.of("start", params.getStart().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                        "end", params.getEnd().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                        "unique", params.isUniqueIp()));

        return objectMapper.convertValue(response, new TypeReference<>() {
        });
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
