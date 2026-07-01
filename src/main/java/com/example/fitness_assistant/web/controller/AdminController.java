package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.exercise.CreateExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.DeleteExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.UpdateExerciseUseCase;
import com.example.fitness_assistant.application.service.food.CreateFoodUseCase;
import com.example.fitness_assistant.application.service.food.DeleteFoodUseCase;
import com.example.fitness_assistant.application.service.food.UpdateFoodUseCase;
import com.example.fitness_assistant.application.service.muscle.CreateMuscleUseCase;
import com.example.fitness_assistant.application.service.muscle.DeleteMuscleUseCase;
import com.example.fitness_assistant.application.service.muscle.UpdateMuscleUseCase;
import com.example.fitness_assistant.application.service.user.DeleteUserUseCase;
import com.example.fitness_assistant.application.service.user.FindUserUseCase;
import com.example.fitness_assistant.application.service.user.UpdateUserUseCase;
import com.example.fitness_assistant.web.dto.request.create.CreateExerciseRequest;
import com.example.fitness_assistant.web.dto.request.create.CreateFoodRequest;
import com.example.fitness_assistant.web.dto.request.create.CreateMuscleRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateExerciseRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateFoodRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateMuscleRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateUserRequest;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import com.example.fitness_assistant.web.dto.response.FoodResponse;
import com.example.fitness_assistant.web.dto.response.MuscleResponse;
import com.example.fitness_assistant.web.dto.response.UserResponse;
import com.example.fitness_assistant.web.mapper.ExerciseWebMapper;
import com.example.fitness_assistant.web.mapper.FoodWebMapper;
import com.example.fitness_assistant.web.mapper.MuscleWebMapper;
import com.example.fitness_assistant.web.mapper.UserWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final FindUserUseCase findUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final CreateFoodUseCase createFoodUseCase;
    private final UpdateFoodUseCase updateFoodUseCase;
    private final DeleteFoodUseCase deleteFoodUseCase;
    private final CreateExerciseUseCase createExerciseUseCase;
    private final UpdateExerciseUseCase updateExerciseUseCase;
    private final DeleteExerciseUseCase deleteExerciseUseCase;
    private final CreateMuscleUseCase createMuscleUseCase;
    private final UpdateMuscleUseCase updateMuscleUseCase;
    private final DeleteMuscleUseCase deleteMuscleUseCase;
    private final UserWebMapper userWebMapper;
    private final FoodWebMapper foodWebMapper;
    private final ExerciseWebMapper exerciseWebMapper;
    private final MuscleWebMapper muscleWebMapper;

    @GetMapping("/users")
    public Page<UserResponse> getAllUsers(@PageableDefault(size = 12) Pageable pageable) {
        return findUserUseCase.findAllUsers(pageable)
                .map(userWebMapper::toResponse);
    }

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable("id") Long userId) {
        return userWebMapper.toResponse(findUserUseCase.findById(userId));
    }

    @PatchMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@Valid @RequestBody UpdateUserRequest request) {
        return userWebMapper.toResponse(
                updateUserUseCase.updateUser(userWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long userId) {
        deleteUserUseCase.deleteUser(userId);
    }

    @PostMapping("/food")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodResponse createFood(@Valid @RequestBody CreateFoodRequest request) {
        return foodWebMapper.toResponse(
                createFoodUseCase.createFood(foodWebMapper.toDomain(request))
        );
    }

    @PatchMapping("/food")
    @ResponseStatus(HttpStatus.OK)
    public FoodResponse updateFood(@Valid @RequestBody UpdateFoodRequest request) {
        return foodWebMapper.toResponse(
                updateFoodUseCase.updateFood(foodWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/food/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable("id") Long foodId) {
        deleteFoodUseCase.deleteFood(foodId);
    }

    @PostMapping("/exercise")
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseResponse createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return exerciseWebMapper.toResponse(
                createExerciseUseCase.createExercise(exerciseWebMapper.toDomain(request))
        );
    }

    @PatchMapping("/exercise")
    @ResponseStatus(HttpStatus.OK)
    public ExerciseResponse updateExercise(@Valid @RequestBody UpdateExerciseRequest request) {
        return exerciseWebMapper.toResponse(
                updateExerciseUseCase.updateExercise(exerciseWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/exercise/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(@PathVariable("id") Long exerciseId) {
        deleteExerciseUseCase.deleteExercise(exerciseId);
    }

    @PostMapping("/muscle")
    @ResponseStatus(HttpStatus.CREATED)
    public MuscleResponse createMuscle(@Valid @RequestBody CreateMuscleRequest request) {
        return muscleWebMapper.toResponse(
                createMuscleUseCase.createMuscle(muscleWebMapper.toDomain(request))
        );
    }

    @PatchMapping("/muscle")
    @ResponseStatus(HttpStatus.OK)
    public MuscleResponse updateMuscle(@Valid @RequestBody UpdateMuscleRequest request) {
        return muscleWebMapper.toResponse(
                updateMuscleUseCase.updateMuscle(muscleWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/muscle/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMuscle(@PathVariable("id") Long muscleId) {
        deleteMuscleUseCase.deleteMuscle(muscleId);
    }
}
