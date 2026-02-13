package com.example.fitness_assistant.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenFoodFactsResponse
        (@JsonProperty("product") Food food) {

    @JsonIgnoreProperties(ignoreUnknown = true)

    public record Food(
            @JsonProperty("product_name") String name,
            String brands,
            Nutriments nutriments
    ){}

    public record Nutriments(
            @JsonProperty("energy-kcal_100g") double kcal,
            @JsonProperty("proteins_100g") double proteins,
            @JsonProperty("fat_100g") double fats,
            @JsonProperty("carbohydrates_100g") double carbs
    ){}
}
