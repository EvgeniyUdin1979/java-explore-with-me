package ru.practicum.client;

import org.springframework.http.HttpStatus;

public interface BaseClient {
    HttpStatus sendStats(SendParams params);
}
