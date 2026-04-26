package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    public Food toDomain(FoodEntity entity) {
        return new Food(
                entity.getId(),
                entity.getName(),
                entity.getBrands(),
                entity.getKcal(),
                entity.getProteins(),
                entity.getFats(),
                entity.getCarbs()
        );
    }

    public FoodEntity toEntity(Food domain) {
        return new FoodEntity(
                domain.getId(),
                domain.getName(),
                domain.getBrands(),
                domain.getKcal(),
                domain.getProteins(),
                domain.getFats(),
                domain.getCarbs()
        );
    }
}