package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateExerciseRequest(
        @NotNull(message = "ID упражнения не может быть пустым")
        Long id,
        @Size(max = 255, message = "Название слишком длинное")
        String exerciseName,
        @Size(max = 1000, message = "Описание слишком длинное")
        String description,
        @Valid
        List<UpdateMuscleRequest> musclesId
) {
    public record UpdateMuscleRequest(
            @NotNull(message = "ID мышцы не может быть пустым")
            Long id
    ) {}
}
