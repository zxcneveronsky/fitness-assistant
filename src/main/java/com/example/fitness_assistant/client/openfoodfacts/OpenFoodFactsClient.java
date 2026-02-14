package com.example.fitness_assistant.client.openfoodfacts;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenFoodFactsClient {

    private final RestClient foodRestClient;

    public OpenFoodFactsClient(@Qualifier("offRestClient") RestClient restClient) {
        this.foodRestClient = restClient;
    }

    public OpenFoodFactsResponse getFoodResponse(String barcode){
        try{
            return foodRestClient.get()
                    .uri("/api/v2/product/{barcode}.json", barcode)
                    .retrieve()
                    .body(OpenFoodFactsResponse.class);
        } catch (Exception e){
            System.err.println("off"+e.getMessage());
            return null;
        }
    }
}

