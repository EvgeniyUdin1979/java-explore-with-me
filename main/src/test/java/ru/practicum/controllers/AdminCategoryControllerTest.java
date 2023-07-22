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
@Sql(scripts = "file:src/test/resources/data/initACC.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/data/resetDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AdminCategoryControllerTest {
    private final String baseUrl = "http://localhost:8080/admin/categories";
    private final MockMvc mvc;

    @Autowired
    AdminCategoryControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void addTest() throws Exception {
        mvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Тесты\"}"))
                .andDo(print()).andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(2),
                        jsonPath("$.name").value("Тесты"));
    }

    @Test
    void addFailTest() throws Exception {
        mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andDo(print()).andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.status").value("BAD_REQUEST"),
                        jsonPath("$.reason").value("Некорректные параметры запроса."),
                        jsonPath("$.message").value("Название категории не может быть меньше 1 и больше 50 символов.Наименование категории не может быть пустым или отсутствовать")
                        );
        mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Концерты\"}"))
                .andDo(print()).andExpectAll(
                        status().isConflict(),
                        jsonPath("$.status").value("CONFLICT"),
                        jsonPath("$.reason").value("Ограничение целостности было нарушено.")
                );
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
                .andExpect(status().isNotFound());
    }

    @Test
    void patchTest() throws Exception {
        mvc.perform(patch(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Гвозди\"}")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("Гвозди"));
    }

    @Test
    void patchFailTest() throws Exception {
        mvc.perform(patch(baseUrl + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Гвозди\"}")).andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.status").value("NOT_FOUND"),
                        jsonPath("$.reason").value("Ошибка при создании категории."),
                        jsonPath("$.message").value("Категория с id 2 не найдена.")
                );
    }
}