package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateWorkoutRequest(
        @NotBlank(message = "Название тренировки не может быть пустым")
        @Size(max = 255, message = "Название тренировки слишком длинное")
        String name,
        @NotEmpty(message = "Список упражнений не может быть пустым")
        List<@NotNull(message = "ID упражнения не может быть пустым") Long> exerciseIds
) {
}
