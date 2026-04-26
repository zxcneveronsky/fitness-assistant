package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllFoodUseCase {

    private final FoodRepository foodRepository;

    public Page<Food> getAllFood(Pageable pageable) {
        return foodRepository.findAll(pageable);
    }
}
