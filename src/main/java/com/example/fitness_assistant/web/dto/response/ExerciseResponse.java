package com.example.fitness_assistant.web.dto.response;

import java.util.List;

public record ExerciseResponse(
        String exerciseName,
        String description,
        List<ExerciseMuscleResponse> muscles
) {
    public record ExerciseMuscleResponse(
            String name
    ) {}
}
