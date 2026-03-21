package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.service.FoodSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/food")
@Slf4j
public class FoodSearchController {
    private final FoodSearchService foodSearchService;

    public FoodSearchController(FoodSearchService foodSearchService) {
        this.foodSearchService = foodSearchService;
    }

    @GetMapping
    public List<FoodSearchDTO> getFoodByName(@RequestParam String name) {
        log.info("Поиск продукта по названию: '{}'", name);
        return foodSearchService.findFoodByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Food addFood(@Valid @RequestBody Food food) {
        log.info("Добавление продукта: {}", food.getName());
        return foodSearchService.addFood(food);
    }

    @PutMapping("/{id}")
    public FoodSearchDTO updateFood(@PathVariable Long id, @Valid @RequestBody Food food) {
        log.info("Обновление продукта с id: {}", id);
        return foodSearchService.updateFood(id, food);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable Long id) {
        log.warn("Удаление продукта с id: {}", id);
        foodSearchService.deleteFood(id);
    }
}