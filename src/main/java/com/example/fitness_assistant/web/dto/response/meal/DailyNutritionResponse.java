package com.example.fitness_assistant.web.dto.response.meal;

public record DailyNutritionResponse(
        Double kcal,
        Double proteins,
        Double fats,
        Double carbs
){
}
