package com.example.fitness_assistant.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
public class OpenFoodFactsClient {

    private final RestClient foodRestClient;

    public OpenFoodFactsClient(RestClient.Builder builder) {
        this.foodRestClient = builder
                .baseUrl("https://world.openfoodfacts.org")
                .build();
    }

    public OpenFoodFactsResponse getFoodResponse(String barcode){
        try{
            return foodRestClient.get()
                    .uri("/api/v2/product/{barcode}.json", barcode)
                    .retrieve()
                    .body(OpenFoodFactsResponse.class);
        } catch (Exception e){
            System.err.println("Error"+e.getMessage());
        }
        return null;
    }
}

