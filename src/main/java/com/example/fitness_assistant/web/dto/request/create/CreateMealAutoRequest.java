package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateMealAutoRequest(
        @NotNull(message = "Название приема пищи не может быть пустым")
        Long id,
        @NotNull(message = "Калории не могут быть пустыми")
        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        Double weight,
        @NotNull(message = "Дата приема пищи должна быть указана")
        @PastOrPresent(message = "Дата приема пищи не может быть в будущем")
        LocalDate consumedAt
) {
}
