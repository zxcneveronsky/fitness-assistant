package com.example.fitness_assistant.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateExerciseRequest(
        @NotNull(message = "ID упражнения не может быть пустым")
        @NotBlank(message = "ID упражнения не может быть пустым")
        Long id,
        @NotBlank(message = "Название не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
        String exerciseName,
        @NotBlank(message = "Описание не может быть пустым")
        @Size(max = 1000, message = "Описание слишком длинное")
        String description,
        @Valid
        List<UpdateMuscleRequest> muscles
) {
    public record UpdateMuscleRequest(
            @NotNull(message = "ID мышцы не может быть пустым")
            @NotBlank(message = "ID мышцы не может быть пустым")
            Long id,
            @NotBlank(message = "Название мышцы не может быть пустым")
            String name
    ) {}
}
