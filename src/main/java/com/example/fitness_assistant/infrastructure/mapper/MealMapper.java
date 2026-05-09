package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.meal.Meal;
import com.example.fitness_assistant.infrastructure.persistence.entity.MealEntity;
import org.springframework.stereotype.Component;

@Component
public class MealMapper {

    public Meal toDomain(MealEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Meal(
                entity.getId(),
                entity.getUser().getId(),
                entity.getName(),
                entity.getBrands(),
                entity.getKcal(),
                entity.getProteins(),
                entity.getFats(),
                entity.getCarbs(),
                entity.getConsumedAt()
        );
    }

    public MealEntity toEntity(Meal domain) {
        if (domain == null) {
            return null;
        }
        return new MealEntity(
                domain.getId(),
                null, // Это поле проставляется в адаптере через GetReferenceById
                domain.getName(),
                domain.getBrands(),
                domain.getKcal(),
                domain.getProteins(),
                domain.getFats(),
                domain.getCarbs(),
                domain.getConsumedAt()
        );
    }

}
