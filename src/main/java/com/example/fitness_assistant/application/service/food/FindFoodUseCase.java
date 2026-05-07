package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindFoodUseCase {

    private final FoodRepository foodRepository;

    public Page<Food> findFood(String name, Pageable pageable) {
        return foodRepository.searchFood(name, pageable);
    }
    @Transactional
    public Food findById(Long id) {
        return foodRepository.findById(id).orElseThrow(()->new FoodNotFoundException(id));
    }
}
