package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateExerciseRequest(
        @NotNull(message = "ID упражнения не может быть пустым")
        Long id,
        @Size(max = 255, message = "Название слишком длинное")
        String name,
        @Size(max = 1000, message = "Описание слишком длинное")
        String description,
        List<@NotNull Long> muscleIds
) {}
