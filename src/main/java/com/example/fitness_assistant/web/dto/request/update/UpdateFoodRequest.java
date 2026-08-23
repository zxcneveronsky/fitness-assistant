package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateFoodRequest(
        @NotNull(message = "ID продукта не может быть пустым")
        Long id,

        @Size(max = 255, message = "Название продукта слишком длинное")
        String name,

        @Size(max = 255, message = "Название бренда слишком длинное")
        String brands,

        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        @Max(value = 10000, message = "Калории не могут быть больше 10000")
        Double kcal,

        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        @Max(value = 1000, message = "Белки не могут быть больше 1000")
        Double proteins,

        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        @Max(value = 1000, message = "Жиры не могут быть больше 1000")
        Double fats,

        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        @Max(value = 1000, message = "Углеводы не могут быть больше 1000")
        Double carbs
) {}
