package com.example.fitness_assistant.web.dto.response;

import com.example.fitness_assistant.core.model.workoutaccess.AccessLevel;

public record WorkoutAccessResponse(
        Long id,
        Long ownerId,
        Long sharedWithUserId,
        Long workoutId,
        String workoutName,
        AccessLevel accessLevel
) {
}
