package com.example.sonarqubetest.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void createTodo() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Write tests", "description": "for sonarqube"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("Write tests"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void createTodoWithBlankTitleReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": ""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllFilteredByCompleted() throws Exception {
        Todo done = new Todo("done", null);
        done.toggle();
        todoRepository.save(done);
        todoRepository.save(new Todo("todo", null));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/api/todos").param("completed", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("done"));
    }

    @Test
    void findByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/todos/{id}", 9999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Todo not found: 9999"));
    }

    @Test
    void updateTodo() throws Exception {
        Todo saved = todoRepository.save(new Todo("old", null));

        mockMvc.perform(put("/api/todos/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "new", "description": "changed", "completed": true}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new"))
                .andExpect(jsonPath("$.description").value("changed"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void toggleTodo() throws Exception {
        Todo saved = todoRepository.save(new Todo("toggle me", null));

        mockMvc.perform(patch("/api/todos/{id}/toggle", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTodo() throws Exception {
        Todo saved = todoRepository.save(new Todo("delete me", null));

        mockMvc.perform(delete("/api/todos/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/todos/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
