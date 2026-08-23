package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;

public record CreateWorkoutAccessRequest(
        @NotNull(message = "ID тренировки не может быть пустым")
        Long workoutId,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный email")
        @Size(max = 255, message = "Email слишком длинный")
        String email,

        @NotNull(message = "Уровень доступа не может быть пустым")
        AccessLevel accessLevel
) {
}
