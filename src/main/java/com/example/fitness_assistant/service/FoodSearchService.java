package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.exception.FoodNotFoundException;
import com.example.fitness_assistant.repository.FoodRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FoodSearchService {
    private final FoodRepository foodRepository;

    public FoodSearchService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    private FoodSearchDTO mapToDTO(Food food) {
        return new FoodSearchDTO(
                food.getName(),
                food.getBrands(),
                food.getKcal(),
                food.getProteins(),
                food.getFats(),
                food.getCarbs()
        );
    }

    public List<FoodSearchDTO> findFoodByName(String name) {
        List<Food> foods = foodRepository.findByNameContainingIgnoreCase(name);
        if (foods.isEmpty()) {
            log.warn("Продукт '{}' не найден", name);
            throw new FoodNotFoundException(name);
        }
        log.debug("Найдено продуктов: {}", foods.size());
        List<FoodSearchDTO> result = new ArrayList<>();
        for (Food food : foods) {
            result.add(mapToDTO(food));
        }
        return result;
    }

    public Food addFood(Food food) {
        Food saved = foodRepository.save(food);
        log.info("Добавлен продукт: {}", saved.getName());
        return saved;
    }

    @Transactional
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
        log.info("Удалён продукт с id: {}", id);
    }

    @Transactional
    public FoodSearchDTO updateFood(Long id, Food foodDetails) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(String.valueOf(id)));
        food.setName(foodDetails.getName());
        food.setBrands(foodDetails.getBrands());
        food.setKcal(foodDetails.getKcal());
        food.setProteins(foodDetails.getProteins());
        food.setFats(foodDetails.getFats());
        food.setCarbs(foodDetails.getCarbs());
        return mapToDTO(foodRepository.save(food));
    }
}