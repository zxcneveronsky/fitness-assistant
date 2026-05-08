package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record UpdateWorkoutSessionRequest(
        @NotNull(message = "ID сессии не может быть пустым")
        Long id,

        @NotNull(message = "Время окончания должно быть указано")
        @PastOrPresent(message = "Время окончания не может быть в будущем")
        LocalDateTime endTime
) {}