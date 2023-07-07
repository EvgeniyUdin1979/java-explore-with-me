package ru.practicum.stats.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stats.exceptions.RequestException;

@RestControllerAdvice
public class CustomAdvice {

    @ExceptionHandler(RequestException.class)
    ResponseEntity<Error> advice(RequestException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
