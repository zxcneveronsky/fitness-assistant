package com.example.fitness_assistant.client.spoonacular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpoonacularUpcResponse(
        @JsonProperty("title") String name,
        @JsonProperty("brand") String brands,
        @JsonProperty("nutrition") Nutrition nutrition
) {
    public SpoonacularUpcResponse food() { return this; }
    public Nutrition nutriments() { return nutrition; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Nutrition(
            @JsonProperty("nutrients") List<Nutrient> nutrients
    ) {
        public double kcal() { return find("Calories"); }
        public double proteins() { return find("Protein"); }
        public double fats() { return find("Fat"); }
        public double carbs() { return find("Carbohydrates"); }

        private double find(String nutrientName) {
            if (nutrients == null) return 0.0;
            return nutrients.stream()
                    .filter(n -> n.name().equalsIgnoreCase(nutrientName))
                    .findFirst()
                    .map(Nutrient::amount)
                    .orElse(0.0);
        }
    }

    public record Nutrient(
            @JsonProperty("name") String name,
            @JsonProperty("amount") double amount
    ) {}
}
