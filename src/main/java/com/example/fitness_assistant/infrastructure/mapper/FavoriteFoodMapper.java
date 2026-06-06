package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.FavoriteFood;
import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteFoodEntity;
import org.springframework.stereotype.Component;

@Component
public class FavoriteFoodMapper {

    public FavoriteFood toDomain(FavoriteFoodEntity entity) {
        if (entity == null) {
            return null;
        }
        return new FavoriteFood(
                entity.getId(),
                entity.getUser().getId(),
                entity.getFood().getId()
        );
    }

    public FavoriteFoodEntity toEntity(FavoriteFood domain) {
        if (domain == null) {
            return null;
        }
        return new FavoriteFoodEntity(
                domain.getId(),
                null,
                null
        );
    }
}
