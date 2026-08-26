package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateMealAutoRequest(
        @NotNull(message = "ID продукта не может быть пустым")
        @Positive(message = "ID продукта должен быть положительным")
        Long id,
        @NotNull(message = "Вес не может быть пустым")
        @Positive(message = "Вес должен быть положительным")
        @Max(value = 5000, message = "Вес не может быть больше 5000 г")
        Double weight,
        @NotNull(message = "Дата приема должна быть указана")
        @PastOrPresent(message = "Дата приема не может быть в будущем")
        LocalDateTime consumedAt
) {
}
