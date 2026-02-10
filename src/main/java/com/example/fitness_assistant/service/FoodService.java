package com.example.fitness_assistant.service;


import com.example.fitness_assistant.dto.RecordFoodDTO;
import com.example.fitness_assistant.repository.FoodAPI;
import com.example.fitness_assistant.repository.RecordFoodAPI;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    private final FoodAPI foodAPI;
    public FoodService(FoodAPI foodAPI){
        this.foodAPI = foodAPI;
    }

    public RecordFoodDTO getFoodData(String barcode) {
        RecordFoodAPI answer = foodAPI.getFoodResponse(barcode);
        if (answer == null || answer.food() == null){
            return new RecordFoodDTO("Error","-",0.0,0.0,0.0,0.0);
        }
        return new RecordFoodDTO(answer.food().name(),
                answer.food().brands(),
                answer.food().nutriments().kcal(),
                answer.food().nutriments().proteins(),
                answer.food().nutriments().fats(),
                answer.food().nutriments().carbs());
    }
}