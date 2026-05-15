package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateFoodUseCase {

    private final FoodRepository foodRepository;

    @Transactional
    public Food createFood(Food food) {
        food.setId(null);
        Food savedFood = foodRepository.save(food);
        log.info("Продукт создан | id={} | название='{}'",
                savedFood.getId(), savedFood.getName());
        return savedFood;
    }
}
