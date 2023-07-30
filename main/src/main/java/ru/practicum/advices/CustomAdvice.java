package ru.practicum.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.exceptions.RequestException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomAdvice {

    private final String reasonBadRequest = "Некорректные параметры запроса.";

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ApiError> handler(RequestException e) {
        return getResponse(e.getCode(), e.getReason(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
   public ResponseEntity<ApiError> handler(DataIntegrityViolationException e) {
        String message = e.getCause().getCause().getMessage();
        return getResponse(HttpStatus.CONFLICT,
                "Ограничение целостности было нарушено.",
                message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ApiError> handleException(MethodArgumentNotValidException ex) {
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
        return getResponse(HttpStatus.BAD_REQUEST, reasonBadRequest, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<ApiError> handleException(ConstraintViolationException ex) {
        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        for (String errorMessage : errorMessages) {
            builder.append(errorMessage);
        }
        String message = builder.toString().trim();
        log.warn(message);
        return getResponse(HttpStatus.BAD_REQUEST, reasonBadRequest, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> typeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Параметр %s не является числом.", ex.getName());
        log.warn("Параметр {} не является числом.", ex.getName());
        return getResponse(HttpStatus.BAD_REQUEST, reasonBadRequest, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> typeMismatchException(HttpMessageNotReadableException ex) {
        String message = String.format("Не верные данные в теле запроса. %s", ex.getMessage());
        log.warn("Параметр {} не является числом.", ex.getMessage());
        return getResponse(HttpStatus.CONFLICT, reasonBadRequest, message);
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
