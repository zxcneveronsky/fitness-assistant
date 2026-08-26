package com.example.fitness_assistant.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ExerciseSearchIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void searchExerciseWithoutFilters() throws Exception {
        mockMvc.perform(get("/api/v1/exercise/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void searchExerciseByName() throws Exception {
        mockMvc.perform(get("/api/v1/exercise/search").param("name", "жим"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void searchExerciseByMuscleId() throws Exception {
        mockMvc.perform(get("/api/v1/exercise/search").param("muscleId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
