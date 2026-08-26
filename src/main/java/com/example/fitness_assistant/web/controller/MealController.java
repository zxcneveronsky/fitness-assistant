package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.meal.CreateMealUseCase;
import com.example.fitness_assistant.application.service.meal.DeleteMealUseCase;
import com.example.fitness_assistant.application.service.meal.FindMealUseCase;
import com.example.fitness_assistant.application.service.meal.GetDailyNutritionUseCase;
import com.example.fitness_assistant.application.service.meal.UpdateMealUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateMealAutoRequest;
import com.example.fitness_assistant.web.dto.request.create.CreateMealManualRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateMealRequest;
import com.example.fitness_assistant.web.dto.response.meal.DailyNutritionResponse;
import com.example.fitness_assistant.web.dto.response.meal.MealResponse;
import com.example.fitness_assistant.web.mapper.DailyNutritionWebMapper;
import com.example.fitness_assistant.web.mapper.MealWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/meal")
@RequiredArgsConstructor
@Validated
public class MealController {

    private final CreateMealUseCase createMealUseCase;
    private final UpdateMealUseCase updateMealUseCase;
    private final FindMealUseCase findMealUseCase;
    private final DeleteMealUseCase deleteMealUseCase;
    private final MealWebMapper mealWebMapper;
    private final GetDailyNutritionUseCase getDailyNutritionUseCase;
    private final DailyNutritionWebMapper dailyNutritionWebMapper;

    @GetMapping("/{id}")
    public MealResponse getMealById(@AuthenticationPrincipal UserDetailsAdapter adapter, @PathVariable("id") Long mealId){
        return mealWebMapper.toResponse(findMealUseCase.findById(adapter.getUserId(), mealId));
    }

    @GetMapping("/search")
    public Page<MealResponse> searchMeal(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date,
            @PageableDefault(size = 12) Pageable pageable) {
        return findMealUseCase.searchMeal(adapter.getUserId(), date, pageable)
                .map(mealWebMapper::toResponse);
    }

    @GetMapping("/daily")
    public DailyNutritionResponse getDailyMeal(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date) {
        return dailyNutritionWebMapper.toResponse(getDailyNutritionUseCase.getDailyNutrition(adapter.getUserId(), date));
    }

    @PostMapping("/manual")
    @ResponseStatus(HttpStatus.CREATED)
    public MealResponse createMealManual(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody CreateMealManualRequest request) {
        return mealWebMapper.toResponse(
                createMealUseCase.createMealManual(adapter.getUserId(), mealWebMapper.toDomain(request))
        );
    }

    @PostMapping("/auto")
    @ResponseStatus(HttpStatus.CREATED)
    public MealResponse createMealAuto(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody CreateMealAutoRequest request) {
        return mealWebMapper.toResponse(
                createMealUseCase.createMealAuto(adapter.getUserId(), request.id(), request.weight(), request.consumedAt())
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public MealResponse updateMeal(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody UpdateMealRequest request) {
        return mealWebMapper.toResponse(
                updateMealUseCase.updateMeal(adapter.getUserId(), mealWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeal(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long mealId) {
        deleteMealUseCase.deleteMeal(adapter.getUserId(), mealId);
    }
}
