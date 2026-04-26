package com.example.fitness_assistant.web.dto.response;

public record FoodResponse(
        String name,
        String brands,
        Double kcal,
        Double proteins,
        Double fats,
        Double carbs
) {}