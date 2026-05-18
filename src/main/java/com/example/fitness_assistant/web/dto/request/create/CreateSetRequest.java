package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateSetRequest (
    @NotNull(message = "ID сессии не может быть пустым")
    Long sessionId,
    @NotNull(message = "ID упражнения не может быть пустым")
    Long exerciseId,
    @NotNull(message = "Вес не может быть пустым")
    @PositiveOrZero(message = "Вес не может быть отрицательным")
    Double weight,
    @NotNull(message = "Повторения не могут быть пустыми")
    @Min(value = 1, message = "Повторения должны быть не менее 1")
    @Max(value = 500, message = "Повторения не могут быть больше 500")
    Integer reps,
    @NotNull(message = "Дата подхода должна быть указана")
    @PastOrPresent(message = "Дата подхода не может быть в будущем")
    LocalDateTime createdAt
) {}
