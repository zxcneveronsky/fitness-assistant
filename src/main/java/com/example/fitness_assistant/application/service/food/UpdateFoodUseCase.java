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
public class UpdateFoodUseCase {

    private final FoodRepository foodRepository;

    @Transactional
    public Food updateFood(Food foodUpdate) {
        Long foodId = foodUpdate.getId();
        Food updatedFood = foodRepository.findById(foodId)
                .map(existingFood -> {
                    existingFood.setName(foodUpdate.getName() != null ? foodUpdate.getName() : existingFood.getName());
                    existingFood.setBrands(foodUpdate.getBrands() != null ? foodUpdate.getBrands() : existingFood.getBrands());
                    existingFood.setKcal(foodUpdate.getKcal() != null ? foodUpdate.getKcal() : existingFood.getKcal());
                    existingFood.setProteins(foodUpdate.getProteins() != null ? foodUpdate.getProteins() : existingFood.getProteins());
                    existingFood.setFats(foodUpdate.getFats() != null ? foodUpdate.getFats() : existingFood.getFats());
                    existingFood.setCarbs(foodUpdate.getCarbs() != null ? foodUpdate.getCarbs() : existingFood.getCarbs());
                    return foodRepository.save(existingFood);
                })
                .orElseThrow(() -> new FoodNotFoundException(foodId));
        log.info("Продукт обновлен | id={}", foodId);
        return updatedFood;
    }
}
