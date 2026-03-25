package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.FoodCreateDTO;
import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.exception.FoodNotFoundException;
import com.example.fitness_assistant.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodSearchService {

    private final FoodRepository foodRepository;

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
        Page<Food> foods = foodRepository.findByNameContainingIgnoreCaseOrBrandsContainingIgnoreCase(
                name, name, pageable);
        if (foods.isEmpty()) {
            log.info("По запросу '{}' ничего не найдено", name);
            return Page.empty(pageable);
        }
        log.info("Найдено {} продуктов по запросу '{}'", foods.getTotalElements(), name);
        return foods.map(this::toDTO);
    }

    @Cacheable(value = "foodById", key = "#id")
    public FoodSearchDTO getFoodById(Long id) {
        return foodRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new FoodNotFoundException("Продукт с ID " + id + " не найден"));
    }

    @CacheEvict(value = "foods", allEntries = true)
    @Transactional
    public FoodSearchDTO addFood(FoodCreateDTO dto) {
        Food save = foodRepository.save(toEntity(dto));
        log.info("Добавлен продукт: {}", save.getName());
        return toDTO(save);
    }

    @CacheEvict(value = {"foods", "foodById"}, key = "#id")
    @Transactional
    public FoodSearchDTO updateFood(Long id, FoodCreateDTO dto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException("Продукт с ID " + id + " не найден"));
        food.setName(dto.name());
        food.setBrands(dto.brands());
        food.setKcal(dto.kcal());
        food.setProteins(dto.proteins());
        food.setFats(dto.fats());
        food.setCarbs(dto.carbs());
        log.info("Обновлён продукт с id: {}", id);
        return toDTO(foodRepository.save(food));
    }

    @CacheEvict(value = {"foods", "foodById"}, key = "#id")
    @Transactional
    public void deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new FoodNotFoundException("Продукт с ID " + id + " не найден");
        }
        foodRepository.deleteById(id);
        log.info("Удалён продукт с id: {}", id);
    }
}