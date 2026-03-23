package com.example.fitness_assistant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ExerciseDTO(
        Long id,
        @NotBlank(message = "Название не может быть пустым")
        String exerciseName,
        String description,
        @NotNull @NotEmpty @Valid
        List<MuscleDTO> muscles
) {
    public record MuscleDTO(
            @NotBlank(message = "Название главной мышцы не может быть пустым")
            String muscleGroup,
            String muscleDetail
    ) {}
}