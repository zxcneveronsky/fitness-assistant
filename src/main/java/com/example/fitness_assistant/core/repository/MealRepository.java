package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.meal.DailyNutrition;
import com.example.fitness_assistant.core.model.meal.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;


public interface MealRepository {
    Optional<Meal> findById(Long id, Long userId);
    Page<Meal> searchMeal(Long userId, LocalDate date, Pageable pageable);
    Meal save(Meal meal);
    void deleteById(Long id, Long userId);
    boolean existsById(Long id, Long userId);
    DailyNutrition getDailyNutrition(Long userId, LocalDate date);

}
