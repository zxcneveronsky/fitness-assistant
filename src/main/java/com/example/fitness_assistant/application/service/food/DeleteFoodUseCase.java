package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteFoodUseCase {

    private final FoodRepository foodRepository;

    @Transactional
    public void deleteFood(Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new FoodNotFoundException(foodId);
        }
        foodRepository.deleteById(foodId);
        log.info("Продукт удален | id={}", foodId);
    }
}
