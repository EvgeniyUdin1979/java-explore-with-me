package ru.practicum.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RequestException extends RuntimeException {

    private final HttpStatus code;
    private final String reason;

    public RequestException(String message, HttpStatus code, String reason) {
        super(message);
        this.code = code;
        this.reason = reason;
    }
}
