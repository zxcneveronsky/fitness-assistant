package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.*;

public record UpdateSetRequest(
        @NotNull(message = "ID подхода не может быть пустым")
        Long id,
        @NotNull(message = "ID сессии не может быть пустым")
        Long sessionId,
        @NotNull(message = "ID упражнения не может быть пустым")
        Long exerciseId,
        @PositiveOrZero(message = "Вес не может быть отрицательным")
        Double weight,
        @Positive(message = "Повторения не могут быть отрицательными")
        @Max(value = 500, message = "Повторения не могут быть больше 500")
        Integer reps
) {
}
