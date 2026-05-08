package com.example.fitness_assistant.web.dto.response;

import java.time.LocalDateTime;

public record SetResponse(
        Long id,
        Long sessionId,
        Long exerciseId,
        Double weight,
        Integer reps,
        LocalDateTime createdAt
) {
}
