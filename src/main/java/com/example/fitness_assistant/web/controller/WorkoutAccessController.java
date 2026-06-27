package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.workout.CopyWorkoutUseCase;
import com.example.fitness_assistant.application.service.workoutaccess.CreateWorkoutAccessUseCase;
import com.example.fitness_assistant.application.service.workoutaccess.DeleteWorkoutAccessUseCase;
import com.example.fitness_assistant.application.service.workoutaccess.FindWorkoutAccessUseCase;
import com.example.fitness_assistant.application.service.workoutaccess.UpdateWorkoutAccessUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateWorkoutAccessRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateWorkoutAccessRequest;
import com.example.fitness_assistant.web.dto.response.WorkoutAccessResponse;
import com.example.fitness_assistant.web.dto.response.workout.WorkoutResponse;
import com.example.fitness_assistant.web.mapper.WorkoutAccessWebMapper;
import com.example.fitness_assistant.web.mapper.WorkoutWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workout")
@RequiredArgsConstructor
@Validated
public class WorkoutAccessController {

    private final CreateWorkoutAccessUseCase createWorkoutAccessUseCase;
    private final FindWorkoutAccessUseCase findWorkoutAccessUseCase;
    private final UpdateWorkoutAccessUseCase updateWorkoutAccessUseCase;
    private final DeleteWorkoutAccessUseCase deleteWorkoutAccessUseCase;
    private final CopyWorkoutUseCase copyWorkoutUseCase;
    private final WorkoutAccessWebMapper workoutAccessWebMapper;
    private final WorkoutWebMapper workoutWebMapper;

    @PostMapping("/access")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutAccessResponse createWorkoutAccess(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody CreateWorkoutAccessRequest request) {
        return workoutAccessWebMapper.toResponse(
                createWorkoutAccessUseCase.createWorkoutAccess(
                        adapter.getUserId(),
                        request.workoutId(),
                        request.email(),
                        request.accessLevel())
        );
    }

    @GetMapping("/{id}/access")
    public List<WorkoutAccessResponse> getWorkoutAccess(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long workoutId) {
        return findWorkoutAccessUseCase.findByWorkoutIdAndOwnerId(adapter.getUserId(), workoutId)
                .stream()
                .map(workoutAccessWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/access/shared-with-me")
    public List<WorkoutAccessResponse> getSharedWithMe(
            @AuthenticationPrincipal UserDetailsAdapter adapter) {
        return findWorkoutAccessUseCase.findAllSharedWithUserId(adapter.getUserId())
                .stream()
                .map(workoutAccessWebMapper::toResponse)
                .toList();
    }

    @PatchMapping("/access")
    @ResponseStatus(HttpStatus.OK)
    public WorkoutAccessResponse updateWorkoutAccess(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody UpdateWorkoutAccessRequest request) {
        return workoutAccessWebMapper.toResponse(
                updateWorkoutAccessUseCase.updateWorkoutAccess(
                        adapter.getUserId(),
                        workoutAccessWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/access/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutAccess(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long accessId) {
        deleteWorkoutAccessUseCase.deleteWorkoutAccess(adapter.getUserId(), accessId);
    }

    @PostMapping("/{id}/copy")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse copyWorkout(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long workoutId) {
        return workoutWebMapper.toResponse(
                copyWorkoutUseCase.copyWorkout(adapter.getUserId(), workoutId)
        );
    }
}
