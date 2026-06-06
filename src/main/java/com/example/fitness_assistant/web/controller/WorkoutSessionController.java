package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.workoutsession.CreateWorkoutSessionUseCase;
import com.example.fitness_assistant.application.service.workoutsession.DeleteWorkoutSessionUseCase;
import com.example.fitness_assistant.application.service.workoutsession.FindWorkoutSessionUseCase;
import com.example.fitness_assistant.application.service.workoutsession.FindAllWorkoutSessionsUseCase;
import com.example.fitness_assistant.application.service.workoutsession.UpdateWorkoutSessionUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateWorkoutSessionRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateWorkoutSessionRequest;
import com.example.fitness_assistant.web.dto.response.WorkoutSessionResponse;
import com.example.fitness_assistant.web.mapper.WorkoutSessionWebMapper;
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
@RequestMapping("/api/v1/workout/session")
@RequiredArgsConstructor
@Validated
public class WorkoutSessionController {

    private final CreateWorkoutSessionUseCase createWorkoutSessionUseCase;
    private final FindWorkoutSessionUseCase findWorkoutSessionUseCase;
    private final FindAllWorkoutSessionsUseCase findAllWorkoutSessionsUseCase;
    private final UpdateWorkoutSessionUseCase updateWorkoutSessionUseCase;
    private final DeleteWorkoutSessionUseCase deleteWorkoutSessionUseCase;
    private final WorkoutSessionWebMapper workoutSessionWebMapper;

    @GetMapping("/{id}")
    public WorkoutSessionResponse getWorkoutSessionById(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long id) {
        return workoutSessionWebMapper.toResponse(
                findWorkoutSessionUseCase.findById(id, adapter.getUserId())
        );
    }

    @GetMapping("/history")
    public Page<WorkoutSessionResponse> getAllWorkoutSession(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PageableDefault(size = 12) Pageable pageable) {
        return findAllWorkoutSessionsUseCase.findAll(adapter.getUserId(), pageable)
                .map(workoutSessionWebMapper::toResponse);
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutSessionResponse createWorkoutSession(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody CreateWorkoutSessionRequest request) {
        return workoutSessionWebMapper.toResponse(
                createWorkoutSessionUseCase.createSession(request.workoutId(), request.startTime(), adapter.getUserId())
        );
    }

    @PatchMapping("/end")
    @ResponseStatus(HttpStatus.OK)
    public WorkoutSessionResponse updateWorkoutSession(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody UpdateWorkoutSessionRequest request) {
        return workoutSessionWebMapper.toResponse(
                updateWorkoutSessionUseCase.updateSession(request.id(), request.endTime(), adapter.getUserId())
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutSession(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long id) {
        deleteWorkoutSessionUseCase.deleteSession(id, adapter.getUserId());
    }
}