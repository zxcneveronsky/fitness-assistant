package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.food.CalculateFoodUseCase;
import com.example.fitness_assistant.application.service.food.FindFoodUseCase;
import com.example.fitness_assistant.web.dto.response.FoodResponse;
import com.example.fitness_assistant.web.mapper.FoodWebMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
@Validated
public class FoodController {

    private final FindFoodUseCase findFoodUseCase;
    private final CalculateFoodUseCase calculateFoodUseCase;
    private final FoodWebMapper foodWebMapper;

    @GetMapping("/{id}")
    public FoodResponse getFoodById(@PathVariable("id") Long foodId){
        return foodWebMapper.toResponse(findFoodUseCase.findById(foodId));
    }

    @GetMapping("/search")
    public Page<FoodResponse> searchFood(
            @RequestParam(required = false) @Size(max = 100, message = "Длина поискового запроса не должна превышать 100 символов") String name,
            @PageableDefault(size = 12) Pageable pageable) {
        return findFoodUseCase.searchFood(name, pageable)
                .map(foodWebMapper::toResponse);
    }

    @GetMapping("/{id}/calculate")
    public FoodResponse calculateNutrition(
            @PathVariable("id") Long foodId,
            @RequestParam @Positive(message = "Вес должен быть положительным")
            @Max(value = 5000, message = "Вес не может быть больше 5000 г") Double weight) {
        return foodWebMapper.toResponse(calculateFoodUseCase.calculateFood(foodId, weight));
    }

}
