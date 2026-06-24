package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.workout.CreateWorkoutUseCase;
import com.example.fitness_assistant.application.service.workout.DeleteWorkoutUseCase;
import com.example.fitness_assistant.application.service.workout.FindWorkoutUseCase;
import com.example.fitness_assistant.application.service.workout.UpdateWorkoutUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/workout")
@RequiredArgsConstructor
@Validated
public class WorkoutController {
    private final CreateWorkoutUseCase createWorkoutUseCase;
    private final DeleteWorkoutUseCase deleteWorkoutUseCase;
    private final FindWorkoutUseCase findWorkoutUseCase;
    private final UpdateWorkoutUseCase updateWorkoutUseCase;
    private final WorkoutWebMapper workoutWebMapper;

    @GetMapping
    public Page<WorkoutResponse> getAllWorkouts(@AuthenticationPrincipal UserDetailsAdapter adapter, @PageableDefault(size = 12) Pageable pageable) {
        return findWorkoutUseCase.findAll(adapter.getUserId(), pageable)
                .map(workoutWebMapper::toResponse);
    }

    @GetMapping("/{id}")
    public WorkoutWithExerciseResponse getWorkoutById(@AuthenticationPrincipal UserDetailsAdapter adapter, @PathVariable Long id){
        return workoutWebMapper.toResponse(findWorkoutUseCase.findById(id, adapter.getUserId()));
    }

    @GetMapping("/search")
    public Page<WorkoutResponse> searchWorkout(@AuthenticationPrincipal UserDetailsAdapter adapter,
                                                @RequestParam(required = false) String name,
                                                @PageableDefault(size = 12) Pageable pageable) {
        return findWorkoutUseCase.searchWorkout(name, adapter.getUserId(), pageable)
                .map(workoutWebMapper::toResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse createWorkout(@AuthenticationPrincipal UserDetailsAdapter adapter, @Valid @RequestBody CreateWorkoutRequest request){
        return workoutWebMapper.toResponse(createWorkoutUseCase.createWorkout(adapter.getUserId(), workoutWebMapper.toDomain(request)));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public WorkoutResponse updateWorkout(@AuthenticationPrincipal UserDetailsAdapter adapter, @Valid @RequestBody UpdateWorkoutRequest request) {
        return workoutWebMapper.toResponse(updateWorkoutUseCase.updateWorkout(adapter.getUserId(), workoutWebMapper.toDomain(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(@AuthenticationPrincipal UserDetailsAdapter adapter, @PathVariable Long id) {
        deleteWorkoutUseCase.deleteWorkout(id, adapter.getUserId());
    }
}
