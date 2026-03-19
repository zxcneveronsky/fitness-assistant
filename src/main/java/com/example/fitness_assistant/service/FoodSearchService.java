package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.entity.Food; // Предполагаемое имя сущности
import com.example.fitness_assistant.exception.FoodNotFoundException;
import com.example.fitness_assistant.repository.FoodRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodSearchService {

    private final FoodRepository foodRepository;

    // Внедрение через конструктор в стиле ExerciseService
    public FoodSearchService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    /**
     * Поиск продукта по штрихкоду.
     * Возвращает DTO с данными о КБЖУ или выбрасывает исключение.
     */
    public FoodSearchDTO findFoodByBarcode(String barcode) {
        return foodRepository.findByBarcode(barcode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new FoodNotFoundException("Продукт с кодом " + barcode));
    }

    /**
     * Приватный метод-маппер для преобразования сущности в DTO.
     * Помогает держать основную логику чистой, как в вашем ExerciseService.
     */
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
}