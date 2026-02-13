package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.service.FoodSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/food")
public class FoodSearchController {
    private final FoodSearchService foodService;

    public FoodSearchController(FoodSearchService foodService){
        this.foodService=foodService;
    }

    @GetMapping("/{barcode}")
    public FoodSearchDTO getFood(@PathVariable String barcode){
        return foodService.getFoodData(barcode);
    }

}