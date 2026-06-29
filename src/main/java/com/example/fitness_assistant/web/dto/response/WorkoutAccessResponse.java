package com.example.fitness_assistant.web.dto.response;

import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;

public record WorkoutAccessResponse(
        Long id,
        String sharedWithUserEmail,
        Long workoutId,
        String workoutName,
        AccessLevel accessLevel
) {
}
