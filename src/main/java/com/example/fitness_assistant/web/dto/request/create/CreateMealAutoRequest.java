package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateMealAutoRequest(
        @NotNull(message = "ID продукта не может быть пустым")
        Long id,
        @NotNull(message = "Вес не может быть пустым")
        @Positive(message = "Вес должен быть положительным")
        Double weight,
        @NotNull(message = "Дата приема пищи должна быть указана")
        @PastOrPresent(message = "Дата приема пищи не может быть в будущем")
        LocalDateTime consumedAt
) {
}
