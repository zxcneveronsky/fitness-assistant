package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateMealManualRequest(
        @NotNull(message = "Название приема пищи не может быть пустым")
        @NotBlank(message = "Название приема пищи не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
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
        Double carbs,

        @NotNull(message = "Дата приема пищи должна быть указана")
        @PastOrPresent(message = "Дата приема пищи не может быть в будущем")
        LocalDateTime consumedAt
) {}
