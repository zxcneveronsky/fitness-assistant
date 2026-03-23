package com.example.fitness_assistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record FoodCreateDTO(
        @NotBlank(message = "Название продукта не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
        String name,

        @Size(max = 255, message = "Название бренда слишком длинное")
        String brands,

        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        double kcal,

        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        double proteins,

        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        double fats,

        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        double carbs
) {}