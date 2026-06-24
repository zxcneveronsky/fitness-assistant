package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateExerciseRequest(
        @NotNull(message = "Название не может быть пустым")
        @NotBlank(message = "Название не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
        String name,
        @NotNull(message = "Описание не может быть пустым")
        @NotBlank(message = "Описание не может быть пустым")
        @Size(max = 1000, message = "Описание слишком длинное")
        String description,
        @NotNull(message = "Список мышц не может быть пустым")
        @NotEmpty(message = "Список мышц не может быть пустым")
        List<@NotNull Long> muscleIds
) {}
