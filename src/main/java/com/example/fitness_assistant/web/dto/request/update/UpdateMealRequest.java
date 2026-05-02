package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdateMealRequest(
        @NotNull(message = "ID записи не может быть пустым")
        Long id,

        @Size(max = 255, message = "Название слишком длинное")
        String name,

        @Size(max = 255, message = "Название бренда слишком длинное")
        String brands,

        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        Double kcal,

        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        Double proteins,

        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        Double fats,

        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        Double carbs,

        @PastOrPresent(message = "Дата приема пищи не может быть в будущем")
        LocalDate consumedAt
) {}

