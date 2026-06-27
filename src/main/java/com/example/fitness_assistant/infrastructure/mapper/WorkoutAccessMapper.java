package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.workoutaccess.WorkoutAccess;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutAccessEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkoutAccessMapper {

    public WorkoutAccess toDomain(WorkoutAccessEntity entity) {
        if (entity == null) {
            return null;
        }
        return new WorkoutAccess(
                entity.getId(),
                entity.getOwner().getId(),
                entity.getSharedWithUser().getId(),
                entity.getSharedWithUser().getEmail(),
                entity.getWorkout().getId(),
                entity.getWorkout().getName(),
                entity.getAccessLevel()
        );
    }

    public WorkoutAccessEntity toEntity(WorkoutAccess domain) {
        if (domain == null) {
            return null;
        }
        return new WorkoutAccessEntity(
                domain.getId(),
                null,
                null,
                null,
                domain.getAccessLevel()
        );
    }
}
