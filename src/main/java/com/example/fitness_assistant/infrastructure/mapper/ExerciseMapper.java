package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.infrastructure.persistence.entity.ExerciseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseMapper {

    private final MuscleMapper muscleMapper;

    public Exercise toDomain(ExerciseEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Exercise(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getMuscles().stream()
                        .map(muscleMapper::toDomain)
                        .toList()
        );
    }

    public ExerciseEntity toEntity(Exercise domain) {
        if (domain == null) {
            return null;
        }
        return new ExerciseEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getMuscles().stream()
                        .map(muscleMapper::toEntity)
                        .toList()
        );
    }
}