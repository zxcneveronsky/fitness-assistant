package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculateFoodUseCase {
    private final FoodRepository foodRepository;
    public Food calculateNutrition(Long id, Double weight) {
        Food food = foodRepository.findById(id).orElseThrow(()->new FoodNotFoundException(id));
        Double k = weight / 100.0;
        food.setKcal(food.getKcal() * k);
        food.setProteins(food.getProteins() * k);
        food.setFats(food.getFats() * k);
        food.setCarbs(food.getCarbs() * k);
        log.info("Пищевая ценность рассчитана | id={} | вес={}", id, weight);
        return food;
    }

}
