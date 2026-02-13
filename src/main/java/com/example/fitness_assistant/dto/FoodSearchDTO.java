package com.example.fitness_assistant.dto;

public record FoodSearchDTO(
    String name,
    String brands,
    double kcal,
    double proteins,
    double fats,
    double carbs
) {}
