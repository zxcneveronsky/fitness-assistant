package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.FavoriteExercise;
import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteExerciseEntity;
import org.springframework.stereotype.Component;

@Component
public class FavoriteExerciseMapper {

    public FavoriteExercise toDomain(FavoriteExerciseEntity entity) {
        if (entity == null) {
            return null;
        }
        return new FavoriteExercise(
                entity.getId(),
                entity.getUser().getId(),
                entity.getExercise().getId()
        );
    }

    public FavoriteExerciseEntity toEntity(FavoriteExercise domain) {
        if (domain == null) {
            return null;
        }
        return new FavoriteExerciseEntity(
                domain.getId(),
                null,
                null
        );
    }
}
