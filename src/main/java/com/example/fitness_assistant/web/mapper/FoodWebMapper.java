package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.web.dto.request.CreateFoodRequest;
import com.example.fitness_assistant.web.dto.request.UpdateFoodRequest;
import com.example.fitness_assistant.web.dto.response.FoodResponse;
import org.springframework.stereotype.Component;

@Component
public class FoodWebMapper {

    public Food toDomain(CreateFoodRequest request) {
        return new Food(
                null,
                request.name(),
                request.brands(),
                request.kcal(),
                request.proteins(),
                request.fats(),
                request.carbs()
        );
    }

    public Food toDomain(UpdateFoodRequest request) {
        return new Food(
                request.id(),
                request.name(),
                request.brands(),
                request.kcal(),
                request.proteins(),
                request.fats(),
                request.carbs()
        );
    }

    public FoodResponse toResponse(Food food) {
        return new FoodResponse(
                food.getName(),
                food.getBrands(),
                food.getKcal(),
                food.getProteins(),
                food.getFats(),
                food.getCarbs()
        );
    }
}
