package com.example.fitness_assistant.web.dto.response;

import java.time.LocalDateTime;

public record WorkoutSessionResponse(
        Long id,
        Long workoutId,
        Long userId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}