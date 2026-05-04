package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.meal.*;
import com.example.fitness_assistant.web.dto.request.create.CreateMealAutoRequest;
import com.example.fitness_assistant.web.dto.request.create.CreateMealManualRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateMealRequest;
import com.example.fitness_assistant.web.dto.response.MealResponse;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/meal")
@RequiredArgsConstructor
public class MealController {

    private final CreateMealUseCase createMealUseCase;
    private final UpdateMealUseCase updateMealUseCase;
    private final FindMealUseCase findMealUseCase;
    private final DeleteMealUseCase deleteMealUseCase;
    private final MealWebMapper mealWebMapper;

    @GetMapping("/search")
    public Page<MealResponse> searchMeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
            @PageableDefault(size = 9) Pageable pageable) {
        return findMealUseCase.findMeal(localDate,userDetails,pageable)
                .map(mealWebMapper::toResponse);
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
