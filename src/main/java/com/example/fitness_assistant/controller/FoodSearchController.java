package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.service.FoodSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/food")
@Slf4j
public class FoodSearchController {
    private final FoodSearchService foodSearchService;
    public FoodSearchController(FoodSearchService foodSearchService){this.foodSearchService = foodSearchService;}

    @GetMapping("/{barcode}")
    public FoodSearchDTO getFoodByBarcode(@PathVariable String barcode) {
        log.info("Запрос на поиск продукта по Штрихкоду: '{}'", barcode);
        return foodSearchService.findFoodByBarcode(barcode);
    }

    @PutMapping("/{barcode}")
    public FoodSearchDTO updateFood(@PathVariable String barcode, @Valid @RequestBody Food food) {
        log.info("Запрос на изменение данных продукта [Штрихкод: {}]", barcode);
        return foodSearchService.updateFood(barcode, food);
    }
}