package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateMuscleRequest(
        @NotNull(message = "ID мышцы не может быть пустым")
        Long id,
        @Size(max = 255, message = "Название слишком длинное")
        String name
) {}
