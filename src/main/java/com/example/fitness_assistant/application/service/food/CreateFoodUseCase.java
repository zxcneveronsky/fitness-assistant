package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateFoodUseCase {

    private final FoodRepository foodRepository;

    @Transactional
    public Food createFood(Food food) {
        food.setId(null);
        return foodRepository.save(food);
    }
}
