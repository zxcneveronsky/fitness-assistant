package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.exception.FoodNotFoundException;
import com.example.fitness_assistant.repository.FoodRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public FoodSearchDTO findFoodByBarcode(String barcode) {

        Optional<Food> foodOptional = foodRepository.findByBarcode(barcode);
        if (foodOptional.isEmpty()) {
            log.warn("Продукт со штрихкодом '{}' не найден", barcode);
            throw new FoodNotFoundException(barcode);
        }
        Food food = foodOptional.get();
        log.debug("Продукт найден: {}, бренд: {}", food.getName(), food.getBrands());
        return mapToDTO(food);
    }


    @Transactional
    public FoodSearchDTO updateFood(String barcode, Food foodDetails) {
        Food food = foodRepository.findByBarcode(barcode)
                .orElseThrow(() -> {
                    log.warn("Ошибка обновления: продукт не найден [Штрихкод: {}]", barcode);
                    return new FoodNotFoundException(barcode);
                });
        food.setName(foodDetails.getName());
        food.setBrands(foodDetails.getBrands());
        food.setKcal(foodDetails.getKcal());
        food.setProteins(foodDetails.getProteins());
        food.setFats(foodDetails.getFats());
        food.setCarbs(foodDetails.getCarbs());

        Food updatedFood = foodRepository.save(food);

        log.info("Данные продукта '{}' обновлены [Штрихкод: {}]", updatedFood.getName(), barcode);
        return mapToDTO(updatedFood);
    }
}