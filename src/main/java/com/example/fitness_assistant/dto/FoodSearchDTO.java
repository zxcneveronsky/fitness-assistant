package com.example.fitness_assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FoodSearchDTO(
        Long id, // Обязательно добавь!
        String name,
        String brands,
        double kcal,
        double proteins,
        double fats,
        double carbs
) {}