package com.example.fitness_assistant.dto;

import java.util.List;



public record ExerciseDTO(
        String exerciseName,
        String description,
        List<MuscleDTO> muscles
) {
    public record MuscleDTO(
            String muscleGroup,
            String muscleDetail
    ) {}
}
