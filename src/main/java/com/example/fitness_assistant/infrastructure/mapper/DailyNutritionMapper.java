package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.meal.DailyNutrition;
import com.example.fitness_assistant.infrastructure.persistence.projection.DailyNutritionProjection;
import org.springframework.stereotype.Component;

@Component
public class DailyNutritionMapper {
    public DailyNutrition toDomain(DailyNutritionProjection projection){
        return new DailyNutrition(
                projection.getKcal(),
                projection.getProteins(),
                projection.getFats(),
                projection.getCarbs()
        );
    }
}
