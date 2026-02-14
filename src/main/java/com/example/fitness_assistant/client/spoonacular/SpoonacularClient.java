package com.example.fitness_assistant.client.spoonacular;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SpoonacularClient {
    private final RestClient restClient;
    private final String apiKey;

    public SpoonacularClient(@Qualifier("spoonacularRestClient") RestClient restClient,
                             @Value("${api.spoonacular.key}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public SpoonacularUpcResponse getProductByUpc(String barcode) {
        try {
            return restClient.get()
                    .uri("/food/products/upc/{upc}?apiKey={key}&includeNutrition=true", barcode, apiKey)
                    .retrieve()
                    .body(SpoonacularUpcResponse.class);
        } catch (Exception e) {
            System.err.println("spp"+e.getMessage());
            return null;
        }
    }
}