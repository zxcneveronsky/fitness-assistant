package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.food.*;
import com.example.fitness_assistant.web.dto.request.CreateFoodRequest;
import com.example.fitness_assistant.web.dto.request.UpdateFoodRequest;
import com.example.fitness_assistant.web.dto.response.FoodResponse;
import com.example.fitness_assistant.web.mapper.FoodWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
@Validated
public class FoodController {

    private final GetAllFoodUseCase getAllFoodUseCase;
    private final FindFoodUseCase findFoodUseCase;
    private final CreateFoodUseCase createFoodUseCase;
    private final UpdateFoodUseCase updateFoodUseCase;
    private final DeleteFoodUseCase deleteFoodUseCase;
    private final CalculateFoodUseCase calculateFoodUseCase;
    private final FoodWebMapper foodWebMapper;

    @GetMapping
    public Page<FoodResponse> getAllFood(@PageableDefault(size = 9) Pageable pageable) {
        return getAllFoodUseCase.getAllFood(pageable)
                .map(foodWebMapper::toResponse);
    }

    @GetMapping("/search")
    public Page<FoodResponse> searchFood(
            @RequestParam String name,
            @PageableDefault(size = 9) Pageable pageable) {
        return findFoodUseCase.findByName(name, pageable)
                .map(foodWebMapper::toResponse);
    }

    @GetMapping("/calc/{id}")
    public FoodResponse calculateNutrition(@PathVariable Long id,@RequestParam Double weight){
        return foodWebMapper.toResponse(calculateFoodUseCase.calculateNutrition(id, weight));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoodResponse createFood(@Valid @RequestBody CreateFoodRequest request) {
        return foodWebMapper.toResponse(
                createFoodUseCase.createFood(foodWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public FoodResponse updateFood(@Valid @RequestBody UpdateFoodRequest request) {
        return foodWebMapper.toResponse(
                updateFoodUseCase.updateFood(foodWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable Long id) {
        deleteFoodUseCase.deleteById(id);
    }
}
