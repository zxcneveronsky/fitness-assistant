package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.FoodDTO;
import com.example.fitness_assistant.service.FoodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/food")
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService){
        this.foodService=foodService;
    }

    @GetMapping("/{barcode}")
    public FoodDTO getFood(@PathVariable String barcode){
        return foodService.getFoodData(barcode);
    }

}