package com.example.fitness_assistant.web.dto.response.workoutsession;

import java.time.LocalDateTime;

public record SetItemResponse(
    Long id,
    Double weight,
    Integer reps,
    LocalDateTime createdAt
) {}
