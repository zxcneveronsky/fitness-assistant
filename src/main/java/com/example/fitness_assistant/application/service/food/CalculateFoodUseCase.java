package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculateFoodUseCase {
    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public Food calculateFood(Long foodId, Double weight) {
        if (weight == null) {
            throw new IllegalArgumentException("Вес не может быть null");
        }
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new FoodNotFoundException(foodId));
        Double weightFactor = weight / 100.0;
        Food calculatedFood = new Food(
                food.getId(),
                food.getName(),
                food.getBrands(),
                food.getKcal() * weightFactor,
                food.getProteins() * weightFactor,
                food.getFats() * weightFactor,
                food.getCarbs() * weightFactor
        );
        log.info("Пищевая ценность рассчитана | id={} | вес={}", foodId, weight);
        return calculatedFood;
    }
}
