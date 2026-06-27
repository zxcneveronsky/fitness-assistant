package com.example.fitness_assistant.web.dto.request.update;

import com.example.fitness_assistant.core.model.workoutaccess.AccessLevel;
import jakarta.validation.constraints.NotNull;

public record UpdateWorkoutAccessRequest(
        @NotNull(message = "ID доступа не может быть пустым")
        Long id,

        AccessLevel accessLevel
) {
}
