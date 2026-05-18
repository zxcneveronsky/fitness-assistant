package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateFoodRequest(
        @NotNull(message = "Название продукта не может быть пустым")
        @NotBlank(message = "Название продукта не может быть пустым")
        @Size(max = 255, message = "Название продукта слишком длинное")
        String name,

        @Size(max = 255, message = "Название бренда слишком длинное")
        String brands,

        @NotNull(message = "Калории не могут быть пустыми")
        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        Double kcal,

        @NotNull(message = "Белки не могут быть пустыми")
        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        Double proteins,

        @NotNull(message = "Жиры не могут быть пустыми")
        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        Double fats,

        @NotNull(message = "Углеводы не могут быть пустыми")
        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        Double carbs
) {}
