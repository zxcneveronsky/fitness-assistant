package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.food.CalculateFoodUseCase;
import com.example.fitness_assistant.application.service.food.CreateFoodUseCase;
import com.example.fitness_assistant.application.service.food.DeleteFoodUseCase;
import com.example.fitness_assistant.application.service.food.FindFoodUseCase;
import com.example.fitness_assistant.application.service.food.UpdateFoodUseCase;
import com.example.fitness_assistant.web.dto.request.create.CreateFoodRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateFoodRequest;
import com.example.fitness_assistant.web.dto.response.FoodResponse;
import com.example.fitness_assistant.web.mapper.FoodWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

    private final FindFoodUseCase findFoodUseCase;
    private final CreateFoodUseCase createFoodUseCase;
    private final UpdateFoodUseCase updateFoodUseCase;
    private final DeleteFoodUseCase deleteFoodUseCase;
    private final CalculateFoodUseCase calculateFoodUseCase;
    private final FoodWebMapper foodWebMapper;

    @GetMapping("/{id}")
    public FoodResponse getFoodById(@PathVariable Long id){
        return foodWebMapper.toResponse(findFoodUseCase.findById(id));
    }

    @GetMapping("/search")
    public Page<FoodResponse> searchFood(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 12) Pageable pageable) {
        return findFoodUseCase.searchFood(name, pageable)
                .map(foodWebMapper::toResponse);
    }

    @GetMapping("/calc/{id}")
    public FoodResponse calculateNutrition(@PathVariable Long id,@RequestParam @Positive Double weight){
        return foodWebMapper.toResponse(calculateFoodUseCase.calculateFood(id, weight));
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
        deleteFoodUseCase.deleteFood(id);
    }
}
