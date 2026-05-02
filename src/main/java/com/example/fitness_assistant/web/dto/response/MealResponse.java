package com.example.fitness_assistant.web.dto.response;

import java.time.LocalDate;

public record MealResponse(
        Long id,
        String name,
        String brands,
        Double kcal,
        Double proteins,
        Double fats,
        Double carbs,
        LocalDate consumedAt
) {}
