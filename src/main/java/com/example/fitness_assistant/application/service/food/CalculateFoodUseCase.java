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
    public Food calculateNutrition(Long id, Double weight) {
        if (weight == null) {
            throw new IllegalArgumentException("Вес не может быть null");
        }
        Food food = foodRepository.findById(id).orElseThrow(()->new FoodNotFoundException(id));
        Double k = weight / 100.0;
        Food calculated = new Food(
                food.getId(),
                food.getName(),
                food.getBrands(),
                food.getKcal() * k,
                food.getProteins() * k,
                food.getFats() * k,
                food.getCarbs() * k
        );
        log.info("Пищевая ценность рассчитана | id={} | вес={}", id, weight);
        return calculated;
    }

}
