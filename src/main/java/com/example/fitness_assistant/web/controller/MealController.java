package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.meal.*;
import com.example.fitness_assistant.web.dto.request.create.CreateMealAutoRequest;
import com.example.fitness_assistant.web.dto.request.create.CreateMealManualRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateMealRequest;
import com.example.fitness_assistant.web.dto.response.meal.DailyNutritionResponse;
import com.example.fitness_assistant.web.dto.response.meal.MealResponse;
import com.example.fitness_assistant.web.mapper.DailyNutritionWebMapper;
import com.example.fitness_assistant.web.mapper.MealWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/meal")
@RequiredArgsConstructor
public class MealController {

    private final CreateMealUseCase createMealUseCase;
    private final UpdateMealUseCase updateMealUseCase;
    private final FindMealUseCase findMealUseCase;
    private final DeleteMealUseCase deleteMealUseCase;
    private final MealWebMapper mealWebMapper;
    private final GetDailyNutritionUseCase getDailyNutritionUseCase;
    private final DailyNutritionWebMapper dailyNutritionWebMapper;

    @GetMapping("/{id}")
    public MealResponse getMealById(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id){
        return mealWebMapper.toResponse(findMealUseCase.findById(id,userDetails));
    }

    @GetMapping("/search")
    public Page<MealResponse> searchMeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime,
            @PageableDefault(size = 9) Pageable pageable) {
        return findMealUseCase.findMeal(localDateTime,userDetails,pageable)
                .map(mealWebMapper::toResponse);
    }

    @GetMapping("/daily")
    public DailyNutritionResponse getDailyMeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        return dailyNutritionWebMapper.toResponse(getDailyNutritionUseCase.getDailyNutrition(localDateTime,userDetails));
    }

    @PostMapping("/manual")
    @ResponseStatus(HttpStatus.CREATED)
    public MealResponse createMealManual(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateMealManualRequest request) {
        return mealWebMapper.toResponse(
                createMealUseCase.createMealManual(userDetails, mealWebMapper.toDomain(request))
        );
    }

    @PostMapping("/auto")
    @ResponseStatus(HttpStatus.CREATED)
    public MealResponse createMealAuto(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateMealAutoRequest request) {
        return mealWebMapper.toResponse(
                createMealUseCase.createMealAuto(userDetails,request.id(),request.weight(),mealWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public MealResponse updateMeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateMealRequest request) {
        return mealWebMapper.toResponse(
                updateMealUseCase.updateMeal(userDetails,mealWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        deleteMealUseCase.deleteMeal(id, userDetails);
    }
}
