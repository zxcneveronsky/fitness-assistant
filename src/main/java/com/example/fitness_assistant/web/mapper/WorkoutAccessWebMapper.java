package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.workoutaccess.WorkoutAccess;
import com.example.fitness_assistant.web.dto.request.update.UpdateWorkoutAccessRequest;
import com.example.fitness_assistant.web.dto.response.WorkoutAccessResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkoutAccessWebMapper {

    public WorkoutAccessResponse toResponse(WorkoutAccess workoutAccess) {
        return new WorkoutAccessResponse(
                workoutAccess.getId(),
                workoutAccess.getSharedWithUserEmail(),
                workoutAccess.getWorkoutId(),
                workoutAccess.getWorkoutName(),
                workoutAccess.getAccessLevel()
        );
    }

    public WorkoutAccess toDomain(UpdateWorkoutAccessRequest request) {
        return new WorkoutAccess(
                request.id(),
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.accessLevel()
        );
    }
}
