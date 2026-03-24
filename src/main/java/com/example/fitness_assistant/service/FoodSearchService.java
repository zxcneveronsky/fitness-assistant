package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.FoodCreateDTO;
import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.exception.FoodNotFoundException;
import com.example.fitness_assistant.repository.FoodRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FoodSearchService {

    private final FoodRepository foodRepository;

    public FoodSearchService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    private FoodSearchDTO toDTO(Food food) {
        return new FoodSearchDTO(
                food.getId(),
                food.getName(),
                food.getBrands(),
                food.getKcal(),
                food.getProteins(),
                food.getFats(),
                food.getCarbs()
        );
    }

    private Food toEntity(FoodCreateDTO dto) {
        Food food = new Food();
        food.setName(dto.name());
        food.setBrands(dto.brands());
        food.setKcal(dto.kcal());
        food.setProteins(dto.proteins());
        food.setFats(dto.fats());
        food.setCarbs(dto.carbs());
        return food;
    }

    @Cacheable(value = "foods", key = "#name.toLowerCase() + '-' + #pageable.pageNumber")
    public Page<FoodSearchDTO> findFoodByName(String name, Pageable pageable) {
        Page<Food> foods = foodRepository.findByNameContainingIgnoreCaseOrBrandsContainingIgnoreCase(name, name, pageable);
        if (foods.isEmpty()) {
            log.warn("Продукт '{}' не найден", name);
            throw new FoodNotFoundException(name);
        }
        log.debug("Найдено продуктов: {}", foods.getTotalElements());
        return foods.map(this::toDTO);
    }

    @CacheEvict(value = "foods", allEntries = true)
    @Transactional
    public FoodSearchDTO addFood(FoodCreateDTO dto) {
        Food saved = foodRepository.save(toEntity(dto));
        log.info("Добавлен продукт: {}", saved.getName());
        return toDTO(saved);
    }

    @CacheEvict(value = "foods", allEntries = true)
    @Transactional
    public FoodSearchDTO updateFood(Long id, FoodCreateDTO dto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(String.valueOf(id)));
        food.setName(dto.name());
        food.setBrands(dto.brands());
        food.setKcal(dto.kcal());
        food.setProteins(dto.proteins());
        food.setFats(dto.fats());
        food.setCarbs(dto.carbs());
        return toDTO(foodRepository.save(food));
    }

    @CacheEvict(value = "foods", allEntries = true)
    @Transactional
    public void deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new FoodNotFoundException(String.valueOf(id));
        }
        foodRepository.deleteById(id);
        log.info("Удалён продукт с id: {}", id);
    }
}