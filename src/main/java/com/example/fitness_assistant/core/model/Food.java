package com.example.fitness_assistant.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    private Long id;
    private String name;
    private String brands;
    private Double kcal;
    private Double proteins;
    private Double fats;
    private Double carbs;

}