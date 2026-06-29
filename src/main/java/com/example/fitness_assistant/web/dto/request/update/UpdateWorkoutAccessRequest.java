package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.NotNull;
import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;

public record UpdateWorkoutAccessRequest(
        @NotNull(message = "ID доступа не может быть пустым")
        Long id,

        AccessLevel accessLevel
) {
}
