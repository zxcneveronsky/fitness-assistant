package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMuscleRequest(
        @NotNull(message = "Название не может быть пустым")
        @NotBlank(message = "Название не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
        String name
) {}
