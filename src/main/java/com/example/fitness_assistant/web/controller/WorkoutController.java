package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.workout.*;
import com.example.fitness_assistant.web.dto.request.create.CreateWorkoutRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateWorkoutRequest;
import com.example.fitness_assistant.web.dto.response.workout.WorkoutResponse;
import com.example.fitness_assistant.web.dto.response.workout.WorkoutWithExerciseResponse;
import com.example.fitness_assistant.web.mapper.WorkoutWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/workout")
@RequiredArgsConstructor
public class WorkoutController {
    private final CreateWorkoutUseCase createWorkoutUseCase;
    private final DeleteWorkoutUseCase deleteWorkoutUseCase;
    private final FindWorkoutUseCase findWorkoutUseCase;
    private final GetAllWorkoutsUseCase getAllWorkoutsUseCase;
    private final UpdateWorkoutUseCase updateWorkoutUseCase;
    private final WorkoutWebMapper workoutWebMapper;

    @GetMapping
    public Page<WorkoutResponse> getAllWorkouts(@AuthenticationPrincipal UserDetails userDetails, @PageableDefault(size = 9) Pageable pageable) {
        return getAllWorkoutsUseCase.getAllWorkouts(userDetails, pageable)
                .map(workoutWebMapper::toResponse);
    }

    @GetMapping("/{id}")
    public WorkoutWithExerciseResponse getWorkoutById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id){
        return workoutWebMapper.toResponse(findWorkoutUseCase.findById(id, userDetails));
    }

    @GetMapping("/search")
    public Page<WorkoutResponse> searchWorkout(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam(required = false) String name,
                                                @PageableDefault(size = 9) Pageable pageable) {
        return findWorkoutUseCase.findWorkout(name, userDetails, pageable)
                .map(workoutWebMapper::toResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse createWorkout(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CreateWorkoutRequest request){
        return workoutWebMapper.toResponse(createWorkoutUseCase.createWorkout(userDetails, workoutWebMapper.toDomain(request)));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public WorkoutResponse updateWorkout(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateWorkoutRequest request) {
        return workoutWebMapper.toResponse(updateWorkoutUseCase.updateWorkout(userDetails, workoutWebMapper.toDomain(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        deleteWorkoutUseCase.deleteWorkout(id, userDetails);
    }
}
