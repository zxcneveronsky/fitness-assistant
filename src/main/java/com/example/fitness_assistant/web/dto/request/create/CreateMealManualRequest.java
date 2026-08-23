package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateMealManualRequest(
        @NotBlank(message = "Название приема пищи не может быть пустым")
        @Size(max = 255, message = "Название приема пищи слишком длинное")
        String name,

        @Size(max = 255, message = "Название бренда слишком длинное")
        String brands,

        @NotNull(message = "Калории не могут быть пустыми")
        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        @Max(value = 10000, message = "Калории не могут быть больше 10000")
        Double kcal,

        @NotNull(message = "Белки не могут быть пустыми")
        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        @Max(value = 1000, message = "Белки не могут быть больше 1000")
        Double proteins,

        @NotNull(message = "Жиры не могут быть пустыми")
        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        @Max(value = 1000, message = "Жиры не могут быть больше 1000")
        Double fats,

        @NotNull(message = "Углеводы не могут быть пустыми")
        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        @Max(value = 1000, message = "Углеводы не могут быть больше 1000")
        Double carbs,

        @NotNull(message = "Дата приема должна быть указана")
        @PastOrPresent(message = "Дата приема не может быть в будущем")
        LocalDateTime consumedAt
) {}
