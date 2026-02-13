package com.example.fitness_assistant.service;


import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.repository.OpenFoodFactsClient;
import com.example.fitness_assistant.repository.OpenFoodFactsResponse;
import org.springframework.stereotype.Service;

@Service
public class FoodSearchService {

    private final OpenFoodFactsClient foodAPI;
    public FoodSearchService(OpenFoodFactsClient foodAPI){
        this.foodAPI = foodAPI;
    }

    public FoodSearchDTO getFoodData(String barcode) {
        OpenFoodFactsResponse answer = foodAPI.getFoodResponse(barcode);
        if (answer == null || answer.food() == null){
            return new FoodSearchDTO("Error","-",0.0,0.0,0.0,0.0);
        }
        return new FoodSearchDTO(answer.food().name(),
                answer.food().brands(),
                answer.food().nutriments().kcal(),
                answer.food().nutriments().proteins(),
                answer.food().nutriments().fats(),
                answer.food().nutriments().carbs());
    }
}