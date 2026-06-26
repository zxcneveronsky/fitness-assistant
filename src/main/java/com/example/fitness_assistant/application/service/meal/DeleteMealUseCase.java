package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.MealNotFoundException;
import com.example.fitness_assistant.core.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteMealUseCase {
    private final MealRepository mealRepository;

    @Transactional
    public void deleteMeal(Long mealId, Long userId) {
        if (!mealRepository.existsById(mealId, userId)) {
            throw new MealNotFoundException(mealId);
        }
        mealRepository.deleteById(mealId, userId);
        log.info("Приём пищи удалён | id={}", mealId);
    }
}
