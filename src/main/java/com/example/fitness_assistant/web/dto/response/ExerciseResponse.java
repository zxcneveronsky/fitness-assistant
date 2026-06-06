package com.example.fitness_assistant.web.dto.response;

import java.util.List;

public record ExerciseResponse(
        Long id,
        String name,
        String description,
        List<ExerciseMuscleResponse> muscles
) {
    public record ExerciseMuscleResponse(
            Long id,
            String name
    ) {}
}
