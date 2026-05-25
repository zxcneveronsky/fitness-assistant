package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateWorkoutRequest(
        @NotNull(message = "ID тренировки не может быть пустым")
        Long id,

        @Size(max = 255, message = "Название слишком длинное")
        String name,

        List<@NotNull(message = "ID упражнения не может быть пустой ссылкой") Long> exerciseIds
) { }