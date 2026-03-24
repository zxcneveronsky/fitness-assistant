package com.example.fitness_assistant.dto;

public record FoodSearchDTO(
        Long id,
        String name,
        String brands,
        double kcal,
        double proteins,
        double fats,
        double carbs
) {}