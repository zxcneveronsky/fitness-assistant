package com.example.fitness_assistant.core.model.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyNutrition {
    private Double kcal;
    private Double proteins;
    private Double fats;
    private Double carbs;
}
