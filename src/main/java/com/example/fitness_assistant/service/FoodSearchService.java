package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.exception.FoodNotFoundException;
import com.example.fitness_assistant.repository.FoodRepository; // Предполагаем наличие репозитория
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodSearchService {

    private final FoodRepository foodRepository;

    /**
     * Поиск продукта по штрихкоду.
     * * @param barcode Штрихкод продукта
     * @return FoodSearchDTO с данными о КБЖУ
     * @throws FoodNotFoundException если по данному штрихкоду ничего не найдено
     */
    public FoodSearchDTO findFoodByBarcode(String barcode) {
        return foodRepository.findByBarcode(barcode)
                .map(food -> new FoodSearchDTO(
                        food.getName(),
                        food.getBrands(),
                        food.getKcal(),
                        food.getProteins(),
                        food.getFats(),
                        food.getCarbs()
                ))
                .orElseThrow(() -> new FoodNotFoundException("Продукт с кодом " + barcode));
    }
}
