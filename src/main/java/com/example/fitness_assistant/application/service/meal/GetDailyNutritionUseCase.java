package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.model.meal.DailyNutrition;
import com.example.fitness_assistant.core.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetDailyNutritionUseCase {
    private final MealRepository mealRepository;
    @Transactional(readOnly = true)
    public DailyNutrition getDailyNutrition(LocalDateTime consumedAt, Long userId){
        DailyNutrition dailyNutrition = mealRepository.getDailyNutrition(consumedAt,userId);
        log.info("Дневная норма питания получена | userId={}", userId);
        return dailyNutrition;
    }
}
