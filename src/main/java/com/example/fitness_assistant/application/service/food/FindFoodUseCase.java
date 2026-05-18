package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindFoodUseCase {

    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public Page<Food> findFood(String name, Pageable pageable) {
        Page<Food> foodPage = foodRepository.searchFood(name, pageable);
        log.info("Поиск продуктов завершён | name='{}' | найдено={} | страница={}/{}",
                name,
                foodPage.getTotalElements(),
                foodPage.getNumber() + 1,
                foodPage.getTotalPages());
        return foodPage;
    }
    @Transactional(readOnly = true)
    public Food findById(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(()->new FoodNotFoundException(id));
        log.info("Продукт найден | id={}", id);
        return food;
    }
}
