package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllFoodUseCase {

    private final FoodRepository foodRepository;

    public Page<Food> getAllFood(Pageable pageable) {
        Page<Food> foodPage = foodRepository.findAll(pageable);
        log.info("Поиск всех продуктов завершён | найдено={} | страница={}/{}",
                foodPage.getTotalElements(),
                foodPage.getNumber() + 1,
                foodPage.getTotalPages());
        return foodPage;
    }
}
