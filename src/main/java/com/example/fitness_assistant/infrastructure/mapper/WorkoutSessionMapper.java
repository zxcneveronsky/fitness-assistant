package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutSessionEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkoutSessionMapper {

    public WorkoutSession toDomain(WorkoutSessionEntity entity) {
        if (entity == null) return null;
        return new WorkoutSession(
                entity.getId(),
                entity.getWorkout().getId(),
                entity.getUser().getId(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

    public WorkoutSessionEntity toEntity(WorkoutSession domain) {
        if (domain == null) return null;
        return new WorkoutSessionEntity(
                domain.getId(),
                null, // Это поле проставляется в адаптере через getReferenceById
                null, // Это поле проставляется в адаптере через getReferenceById
                domain.getStartTime(),
                domain.getEndTime()
        );
    }
}