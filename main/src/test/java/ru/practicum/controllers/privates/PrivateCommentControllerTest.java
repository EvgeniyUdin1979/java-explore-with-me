package ru.practicum.controllers.privates;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "file:src/test/resources/data/initPCC.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/data/resetDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PrivateCommentControllerTest {
    private final String baseUrl = "http://localhost:8080/users";

    private final MockMvc mvc;

    @Autowired
    PrivateCommentControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void addComment() throws Exception {
        mvc.perform(post(baseUrl + "/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\": \"Hello World\"}")
                .queryParam("eventId", "1")
        ).andDo(print()).andExpect(status().isCreated());
    }

    @Test
    void updateComment() throws Exception {
        mvc.perform(post(baseUrl + "/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\": \"Hello World\"}")
                .queryParam("eventId", "1")
        ).andDo(print()).andExpect(status().isCreated());

        mvc.perform(patch(baseUrl + "/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("eventId", "1")
                .queryParam("text", "123")
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void deleteComment() throws Exception {
        mvc.perform(post(baseUrl + "/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\": \"Hello World\"}")
                .queryParam("eventId", "1")
        ).andDo(print()).andExpect(status().isCreated());

        mvc.perform(delete(baseUrl + "/1/comments/1")

        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getCommentById() throws Exception {
        mvc.perform(get(baseUrl + "/1/comments/1")).andDo(print()).andExpect(status().isNotFound());
    }
}
