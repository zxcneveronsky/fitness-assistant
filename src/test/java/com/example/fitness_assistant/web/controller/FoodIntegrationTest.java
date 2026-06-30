package com.example.fitness_assistant.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FoodIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void getFoodByIdTest() throws Exception{
        mockMvc.perform(get("/api/v1/food/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void searchFoodTest() throws Exception{
        mockMvc.perform(get("/api/v1/food/search").param("name","молоко"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value(containsString("молоко")));
    }

    @Test
    public void calculateNutritionTest() throws Exception{
        mockMvc.perform(get("/api/v1/food/{id}/calculate",1L)
                        .param("weight","200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.kcal").value(274))
                .andExpect(jsonPath("$.proteins").value(59.6))
                .andExpect(jsonPath("$.fats").value(3.6))
                .andExpect(jsonPath("$.carbs").value(0.0));


    }
}
