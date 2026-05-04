package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindFoodUseCase {

    private final FoodRepository foodRepository;

    public Page<Food> findFood(String name, Pageable pageable) {
        return foodRepository.searchFood(name, pageable);
    }
}
