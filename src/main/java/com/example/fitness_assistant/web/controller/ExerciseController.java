package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.exercise.CreateExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.DeleteExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.FindExerciseUseCase;
import com.example.fitness_assistant.application.service.exercise.GetAllExercisesUseCase;
import com.example.fitness_assistant.application.service.exercise.UpdateExerciseUseCase;
import com.example.fitness_assistant.web.dto.request.CreateExerciseRequest;
import com.example.fitness_assistant.web.dto.request.UpdateExerciseRequest;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import com.example.fitness_assistant.web.mapper.ExerciseWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final GetAllExercisesUseCase getAllExercisesUseCase;
    private final FindExerciseUseCase findExerciseUseCase;
    private final CreateExerciseUseCase createExerciseUseCase;
    private final UpdateExerciseUseCase updateExerciseUseCase;
    private final DeleteExerciseUseCase deleteExerciseUseCase;
    private final ExerciseWebMapper exerciseWebMapper;

    @GetMapping
    public Page<ExerciseResponse> getAllExercise(@PageableDefault(size = 9) Pageable pageable) {
        return getAllExercisesUseCase.getAllExercises(pageable)
                .map(exerciseWebMapper::toResponse);
    }

    @GetMapping("/search")
    public Page<ExerciseResponse> searchExercises(
            @RequestParam String name,
            @PageableDefault(size = 9) Pageable pageable) {
        return findExerciseUseCase.findByName(name, pageable)
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
        deleteExerciseUseCase.deleteById(id);
    }
}
