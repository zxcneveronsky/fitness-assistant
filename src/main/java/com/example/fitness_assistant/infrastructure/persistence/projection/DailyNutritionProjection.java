package com.example.fitness_assistant.infrastructure.persistence.projection;

public interface DailyNutritionProjection {
    Double getKcal();
    Double getProteins();
    Double getFats();
    Double getCarbs();
}
