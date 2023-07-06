package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class StatsDto {
    @NotBlank(message = "Имя сервиса не может отсутствовать или быть пустым")
    private String app;

    @NotBlank(message = "Uri сервиса не может отсутствовать или быть пустым")
    private String uri;

    @NotBlank(message = "ip не может отсутствовать или быть пустым")
    private String ip;

    @NotNull(message = "Время обращения к сервису не может отсутствовать")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
