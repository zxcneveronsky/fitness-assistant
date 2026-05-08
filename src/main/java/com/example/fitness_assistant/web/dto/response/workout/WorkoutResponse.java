package com.example.fitness_assistant.web.dto.response.workout;

import java.util.List;

public record WorkoutResponse(
        Long id,
        String name,
        List<Long> exerciseIds
) { }
