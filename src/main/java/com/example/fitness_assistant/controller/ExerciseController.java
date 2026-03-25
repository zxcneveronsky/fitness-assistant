package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.service.ExerciseService;
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

    private final ExerciseService exerciseService;

    @GetMapping
    public Page<ExerciseDTO> getAllExercisePaged(
            @PageableDefault(size = 10) Pageable pageable) {
        return exerciseService.getAllExercisePaged(pageable);
    }

    @GetMapping("/{id}")
    public ExerciseDTO getExerciseById(@PathVariable Long id) {
        return exerciseService.getExerciseById(id);
    }

    @GetMapping("/muscle/{muscle}")
    public Page<ExerciseDTO> getExercisesByMuscle(
            @PathVariable String muscle,
            @PageableDefault(size = 10) Pageable pageable) {
        return exerciseService.getExercisesByMuscle(muscle, pageable);
    }

    @GetMapping("/search")
    public Page<ExerciseDTO> searchExercises(
            @RequestParam String name,
            @PageableDefault(size = 9) Pageable pageable) {
        return exerciseService.searchExercises(name, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseDTO addExercise(@Valid @RequestBody ExerciseDTO dto) {
        return exerciseService.addExercise(dto);
    }

    @PutMapping("/{id}")
    public ExerciseDTO updateExercise(
            @PathVariable Long id,
            @Valid @RequestBody ExerciseDTO dto) {
        return exerciseService.updateExercise(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
    }
}