package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.workoutsession.*;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workout/session")
@RequiredArgsConstructor
public class WorkoutSessionController {

    private final CreateWorkoutSessionUseCase createWorkoutSessionUseCase;
    private final FindWorkoutSessionUseCase findWorkoutSessionUseCase;
    private final GetAllWorkoutSessionsUseCase getAllWorkoutSessionsUseCase;
    private final UpdateWorkoutSessionUseCase updateWorkoutSessionUseCase;
    private final DeleteWorkoutSessionUseCase deleteWorkoutSessionUseCase;
    private final WorkoutSessionWebMapper workoutSessionWebMapper;

    @GetMapping("/{id}")
    public WorkoutSessionResponse getWorkoutSessionById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return workoutSessionWebMapper.toResponse(
                findWorkoutSessionUseCase.findById(id, userDetails)
        );
    }

    @GetMapping("/history")
    public Page<WorkoutSessionResponse> getAllWorkoutSession(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 9) Pageable pageable) {
        return getAllWorkoutSessionsUseCase.getAllSessions(userDetails, pageable)
                .map(workoutSessionWebMapper::toResponse);
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutSessionResponse createWorkoutSession(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateWorkoutSessionRequest request) {
        return workoutSessionWebMapper.toResponse(
                createWorkoutSessionUseCase.createSession(request.workoutId(), request.startTime(), userDetails)
        );
    }

    @PatchMapping("/end")
    @ResponseStatus(HttpStatus.OK)
    public WorkoutSessionResponse updateWorkoutSession(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateWorkoutSessionRequest request) {
        return workoutSessionWebMapper.toResponse(
                updateWorkoutSessionUseCase.updateSession(request.id(), request.endTime(), userDetails)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutSession(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        deleteWorkoutSessionUseCase.deleteSession(id, userDetails);
    }
}