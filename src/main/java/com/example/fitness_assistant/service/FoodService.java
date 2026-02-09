package com.example.fitness_assistant.service;


import com.example.fitness_assistant.dto.FoodDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class FoodService {

    private final RestClient foodRestClient;

    public FoodService(RestClient.Builder builder) {
        this.foodRestClient = builder
                .baseUrl("https://world.openfoodfacts.org")
                .build();
    }

    public FoodDTO getFoodData(String barcode) {
        try {
            Map<String, Object> response = foodRestClient.get()
                    .uri("/api/v2/product/{barcode}.json", barcode)
                    .retrieve()
                    .body(Map.class);

            if (response == null || !response.containsKey("product")) {
                return new FoodDTO("Не найден", "-", 0, 0, 0, 0);
            }

            Map<String, Object> productData = (Map<String, Object>) response.get("product");

            String name = productData.getOrDefault("product_name", "Неизвестно").toString();
            String brand = productData.getOrDefault("brands", "-").toString();

            Map<String, Object> nuts = (Map<String, Object>) productData.get("nutriments");

            double kcal = 0, p = 0, f = 0, c = 0;

            if (nuts != null) {
                kcal = extractDouble(nuts, "energy-kcal_100g");
                p = extractDouble(nuts, "proteins_100g");
                f = extractDouble(nuts, "fat_100g");
                c = extractDouble(nuts, "carbohydrates_100g");
            }

            return new FoodDTO(name, brand, kcal, p, f, c);

        } catch (Exception e) {
            e.printStackTrace();
            return new FoodDTO("Ошибка API", "-", 0, 0, 0, 0);
        }
    }

    private double extractDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0.0;
        return Double.parseDouble(value.toString());
    }
}