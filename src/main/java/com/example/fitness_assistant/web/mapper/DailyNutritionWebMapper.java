package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.meal.DailyNutrition;
import com.example.fitness_assistant.web.dto.response.meal.DailyNutritionResponse;
import org.springframework.stereotype.Component;

@Component
public class DailyNutritionWebMapper {
    public DailyNutritionResponse toResponse(DailyNutrition dailyNutrition){
        return new DailyNutritionResponse(
                dailyNutrition.getKcal(),
                dailyNutrition.getProteins(),
                dailyNutrition.getFats(),
                dailyNutrition.getCarbs()
        );
    }

}
