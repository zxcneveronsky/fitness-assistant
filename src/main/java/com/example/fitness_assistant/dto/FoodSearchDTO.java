package com.example.fitness_assistant.dto;

import jakarta.persistence.Column;

public record FoodSearchDTO(
     String name,
     String brands,
     double kcal,
     double proteins,
     double fats,
     double carbs) {}
