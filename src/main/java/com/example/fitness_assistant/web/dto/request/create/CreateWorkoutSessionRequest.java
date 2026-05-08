package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record CreateWorkoutSessionRequest(
        @NotNull(message = "ID тренировки не может быть пустым")
        Long workoutId,

        @NotNull(message = "Время начала должно быть указано")
        @PastOrPresent(message = "Время начала не может быть в будущем")
        LocalDateTime startTime
) {}