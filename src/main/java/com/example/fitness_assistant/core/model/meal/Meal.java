package com.example.fitness_assistant.core.model.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    private Long id;
    private Long userId;
    private String name;
    private String brands;
    private Double kcal;
    private Double proteins;
    private Double fats;
    private Double carbs;

    private LocalDateTime consumedAt;
}
