package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.MealNotFoundException;
import com.example.fitness_assistant.core.model.meal.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.fitness_assistant.core.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindMealUseCase {
    private final MealRepository mealRepository;

    @Transactional(readOnly = true)
    public Page<Meal> searchMeal(LocalDateTime localDateTime, Long userId, Pageable pageable){
        Page<Meal> meals = mealRepository.searchMeal(localDateTime,userId,pageable);
        log.info("Поиск приёмов пищи завершён | userId={} | найдено={} | страница={}/{}",
                userId, meals.getTotalElements(), meals.getNumber() + 1, meals.getTotalPages());
        return meals;
    }

    @Transactional(readOnly = true)
    public Meal findById(Long id, Long userId){
        Meal meal = mealRepository.findById(id,userId).orElseThrow(()->new MealNotFoundException(id));
        log.info("Приём пищи найден | id={}", id);
        return meal;
    }
}
