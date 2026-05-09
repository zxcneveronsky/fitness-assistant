package com.example.fitness_assistant.web.dto.response.meal;

import java.time.LocalDateTime;

public record MealResponse(
        Long id,
        String name,
        String brands,
        Double kcal,
        Double proteins,
        Double fats,
        Double carbs,
        LocalDateTime consumedAt
) {}
