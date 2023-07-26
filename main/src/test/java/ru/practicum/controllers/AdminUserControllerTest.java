package ru.practicum.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "file:src/test/resources/data/initAUC.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/data/resetDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AdminUserControllerTest {
    private final String baseUrl = "http://localhost:8080/admin/users";
    private final MockMvc mvc;

    @Autowired
    AdminUserControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void addTest() throws Exception {
        mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"ivan.petrov@practicummail.ru\",\"name\": \"Иван Петров\"}"))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                        jsonPath("$.id").value(2),
                        jsonPath("$.email").value("ivan.petrov@practicummail.ru"),
                        jsonPath("$.name").value("Иван Петров"));
    }

    @Test
    void addFailTest() throws Exception {
        mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"ivan.petrov@practicummail.ru\",\"name\": \"\"}"))
                .andDo(print())
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.status").value("BAD_REQUEST"),
                        jsonPath("$.reason").value("Некорректные параметры запроса."),
                        jsonPath("$.message").value("Имя не может быть меньше 2 и больше 250 символов.Имя не может отсутствовать или быть пустым."));

        mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"ivan.petrovpracticummail.ru\",\"name\": \"Иван Петров\"}"))
                .andDo(print())
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.status").value("BAD_REQUEST"),
                        jsonPath("$.reason").value("Некорректные параметры запроса."),
                        jsonPath("$.message").value("Электронный адрес не корректный."));

        mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user1@yandex.ru\",\"name\": \"Иван Петров\"}"))
                .andDo(print())
                .andExpectAll(status().isConflict(),
                        jsonPath("$.status").value("CONFLICT"),
                        jsonPath("$.reason").value("Ограничение целостности было нарушено."));
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete(baseUrl + "/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFailTest() throws Exception {
        mvc.perform(delete(baseUrl + "/2"))
                .andDo(print())
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.status").value("NOT_FOUND"),
                        jsonPath("$.reason").value("Ошибка при удалении пользователя."),
                        jsonPath("$.message").value("Пользователь с id 2 не найден."));
    }

    @Test
    void getTest() throws Exception {
        mvc.perform(get(baseUrl)
                        .queryParam("ids", "1", "2")
                        .queryParam("from", "1")
                        .queryParam("size", "2")
                )

                .andDo(print())
                .andExpect(status().isOk());
    }


}