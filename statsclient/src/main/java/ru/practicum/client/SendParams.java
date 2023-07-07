package ru.practicum.client;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SendParams {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime created = LocalDateTime.now();
}
