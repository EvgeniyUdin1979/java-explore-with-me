package ru.practicum;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "\"Explore With Me\" API сервер",
                description = "Documentation \"Explore With Me\" API v1.0.\n" +
                        "   Описание только эндпоинтов связанных с комментариями."
        )
)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}