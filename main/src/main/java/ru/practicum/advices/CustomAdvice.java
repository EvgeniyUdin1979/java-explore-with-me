package ru.practicum.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.exceptions.RequestException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomAdvice {

    @ExceptionHandler(RequestException.class)
    ResponseEntity<ApiError> handler(RequestException e) {
        return getResponse(e.getCode(), e.getReason(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiError> handler(DataIntegrityViolationException e) {
        String message = e.getCause().getCause().getMessage();
        return getResponse(HttpStatus.CONFLICT,
                "Ограничение целостности было нарушено.",
                message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .sorted()
                .collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        for (String errorMessage : errorMessages) {
            builder.append(errorMessage);
        }
        String message = builder.toString().trim();
        log.warn(message);
        return getResponse(HttpStatus.BAD_REQUEST, "Некорректные параметры запроса.", message);
    }

    private ResponseEntity<ApiError> getResponse(@NonNull HttpStatus status, String reason, String message) {
        ApiError error = ApiError.builder()
                .status(status.name())
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        return new ResponseEntity<>(error, status);
    }
}
