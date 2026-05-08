package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WorkoutMapper {

    public Workout toDomain(WorkoutEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Workout(
                entity.getId(),
                entity.getUser().getId(),
                entity.getName(),
                new ArrayList<>(entity.getExerciseIds())
        );
    }

    public WorkoutEntity toEntity(Workout domain) {
        if (domain == null) {
            return null;
        }
        return new WorkoutEntity(
                domain.getId(),
                null,  // Это поле проставляется в адаптере через GetReferenceById
                domain.getName(),
                new ArrayList<>(domain.getExercisesIds())
        );
    }
}