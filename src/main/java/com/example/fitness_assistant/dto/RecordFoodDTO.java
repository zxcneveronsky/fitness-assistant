package com.example.fitness_assistant.dto;

public record RecordFoodDTO(
    String name,
    String brands,
    double kcal,
    double proteins,
    double fats,
    double carbs
) {}
