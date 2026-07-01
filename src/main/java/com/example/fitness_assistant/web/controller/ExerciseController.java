package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.exercise.FindExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.FindExerciseHistoryUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.response.ExerciseHistoryResponse;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import com.example.fitness_assistant.web.mapper.ExerciseHistoryWebMapper;
import com.example.fitness_assistant.web.mapper.ExerciseWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
@Validated
public class ExerciseController {

    private final FindExerciseUseCase findExerciseUseCase;
    private final FindExerciseHistoryUseCase findExerciseHistoryUseCase;
    private final ExerciseWebMapper exerciseWebMapper;
    private final ExerciseHistoryWebMapper exerciseHistoryWebMapper;

    @GetMapping("/{id}")
    public ExerciseResponse getExerciseById(@PathVariable("id") Long exerciseId) {
        return exerciseWebMapper.toResponse(findExerciseUseCase.findById(exerciseId));
    }

    @GetMapping("/{id}/history")
    public Page<ExerciseHistoryResponse> getExerciseHistory(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long exerciseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 12) Pageable pageable) {
        return findExerciseHistoryUseCase.findExerciseHistory(adapter.getUserId(), exerciseId, from, to, pageable)
                .map(exerciseHistoryWebMapper::toResponse);
    }

    @GetMapping("/search")
    public Page<ExerciseResponse> searchExercises(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long muscleId,
            @PageableDefault(size = 12) Pageable pageable) {
        return findExerciseUseCase.searchExercise(name, muscleId, pageable)
                .map(exerciseWebMapper::toResponse);
    }

}
