package com.example.fitness_assistant.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
public class FoodAPI {

    private final RestClient foodRestClient;

    public FoodAPI(RestClient.Builder builder) {
        this.foodRestClient = builder
                .baseUrl("https://world.openfoodfacts.org")
                .build();
    }

    public RecordFoodAPI getFoodResponse(String barcode){
        try{
            return foodRestClient.get()
                    .uri("/api/v2/product/{barcode}.json", barcode)
                    .retrieve()
                    .body(RecordFoodAPI.class);
        } catch (Exception e){
            System.err.println("Error"+e.getMessage());
        }
        return null;
    }
}

