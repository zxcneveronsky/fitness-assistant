package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.service.FoodSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
public class FoodSearchController {

    private final FoodSearchService foodSearchService;

    /**
     * Поиск продукта по штрихкоду.
     * Пример запроса: GET /api/v1/food/4601234567890
     *
     * @param barcode Штрихкод из пути запроса
     * @return FoodSearchDTO с данными о продукте
     */
    @GetMapping("/{barcode}")
    public ResponseEntity<FoodSearchDTO> getFoodByBarcode(@PathVariable String barcode) {
        FoodSearchDTO food = foodSearchService.findFoodByBarcode(barcode);
        return ResponseEntity.ok(food);
    }
}