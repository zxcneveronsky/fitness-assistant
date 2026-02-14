package com.example.fitness_assistant.service;


import com.example.fitness_assistant.client.spoonacular.SpoonacularClient;
import com.example.fitness_assistant.client.spoonacular.SpoonacularUpcResponse;
import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.client.openfoodfacts.OpenFoodFactsClient;
import com.example.fitness_assistant.client.openfoodfacts.OpenFoodFactsResponse;
import org.springframework.stereotype.Service;

@Service
public class FoodSearchService {

    private final OpenFoodFactsClient openFoodFactsAPI;
    private final SpoonacularClient spoonacularAPI;

    public FoodSearchService(OpenFoodFactsClient openFoodFactsAPI,SpoonacularClient spoonacularAPI){
        this.openFoodFactsAPI = openFoodFactsAPI;
        this.spoonacularAPI = spoonacularAPI;
    }
    public FoodSearchDTO getFoodData(String barcode) {

        System.out.println();

        SpoonacularUpcResponse answer2 = spoonacularAPI.getProductByUpc(barcode);

        if (answer2 == null || answer2.nutrition() == null
        || barcode.equals("3017620422003")
        ){
            System.out.println("Spoonacular == null");
            OpenFoodFactsResponse answer = openFoodFactsAPI.getFoodResponse(barcode);
            if (answer == null || answer.food() == null){
                System.out.println("OpenFoodFacts == null");
                return new FoodSearchDTO("Error", "-", 0.0, 0.0, 0.0, 0.0);
            }
            System.out.println("OpenFoodFacts !");
            return new FoodSearchDTO(answer.food().name(),
                    answer.food().brands(),
                    answer.food().nutriments().kcal(),
                    answer.food().nutriments().proteins(),
                    answer.food().nutriments().fats(),
                    answer.food().nutriments().carbs());

        }
        System.out.println("Spoonacular !");
        return new FoodSearchDTO(answer2.food().name(),
                answer2.food().brands(),
                answer2.food().nutriments().kcal(),
                answer2.food().nutriments().proteins(),
                answer2.food().nutriments().fats(),
                answer2.food().nutriments().carbs());


    }

}