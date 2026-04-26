package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExistsFoodUseCase {

    private final FoodRepository foodRepository;

    public boolean existsById(Long id) {
        return foodRepository.existsById(id);
    }
}
