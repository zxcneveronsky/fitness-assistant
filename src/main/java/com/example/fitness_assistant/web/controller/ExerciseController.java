package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.exercise.CreateExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.DeleteExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.FindExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.UpdateExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.FindExerciseHistoryUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateExerciseRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateExerciseRequest;
import com.example.fitness_assistant.web.dto.response.ExerciseHistoryResponse;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import com.example.fitness_assistant.web.mapper.ExerciseHistoryWebMapper;
import com.example.fitness_assistant.web.mapper.ExerciseWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
@Validated
public class ExerciseController {

    private final FindExerciseUseCase findExerciseUseCase;
    private final CreateExerciseUseCase createExerciseUseCase;
    private final UpdateExerciseUseCase updateExerciseUseCase;
    private final DeleteExerciseUseCase deleteExerciseUseCase;
    private final FindExerciseHistoryUseCase findExerciseHistoryUseCase;
    private final ExerciseWebMapper exerciseWebMapper;
    private final ExerciseHistoryWebMapper exerciseHistoryWebMapper;

    @GetMapping("/{id}")
    public ExerciseResponse getExerciseById(@PathVariable Long id) {
        return exerciseWebMapper.toResponse(findExerciseUseCase.findById(id));
    }

    @GetMapping("/{id}/history")
    public List<ExerciseHistoryResponse> getExerciseHistory(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return findExerciseHistoryUseCase.findExerciseHistory(id, adapter.getUserId(), from, to)
                .stream()
                .map(exerciseHistoryWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/search")
    public Page<ExerciseResponse> searchExercises(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long muscleId,
            @PageableDefault(size = 12) Pageable pageable) {
        return findExerciseUseCase.searchExercise(name, muscleId, pageable)
                .map(exerciseWebMapper::toResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseResponse createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return exerciseWebMapper.toResponse(
                createExerciseUseCase.createExercise(exerciseWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ExerciseResponse updateExercise(@Valid @RequestBody UpdateExerciseRequest request) {
        return exerciseWebMapper.toResponse(
                updateExerciseUseCase.updateExercise(exerciseWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(@PathVariable Long id) {
        deleteExerciseUseCase.deleteExercise(id);
    }
}
