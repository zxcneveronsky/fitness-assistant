package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
@Slf4j
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public Page<ExerciseDTO> getAllExercisePaged(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Запрос на пагинацию: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return exerciseService.getAllExercisePaged(pageable);
    }

    @GetMapping("/muscle/{muscle}")
    public Page<ExerciseDTO> getExerciseByMuscle(
            @PathVariable String muscle,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Поиск упражнений для группы мышц: {}", muscle);
        return exerciseService.getExercisesByMuscle(muscle, pageable);
    }

    @GetMapping("/exercise/{exercise}")
    public ExerciseDTO getExerciseByName(@PathVariable String exercise){
        log.info("Поиск информации по упражнению: {}", exercise);
        return exerciseService.getExerciseByName(exercise);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseDTO addExercise(@Valid @RequestBody ExerciseDTO exercise) {
        return exerciseService.addExercise(exercise);
    }

    @DeleteMapping("/{exerciseName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(@PathVariable String exerciseName) {
        log.warn("Удаление упражнения: {}", exerciseName);
        exerciseService.deleteExercise(exerciseName);
    }
    @GetMapping("/search")
    public Page<ExerciseDTO> searchExercises(
            @RequestParam String name,
            @PageableDefault(size = 9) Pageable pageable) {
        log.info("Поиск упражнений по запросу: '{}', страница: {}", name, pageable.getPageNumber());
        return exerciseService.searchExercisesByName(name, pageable);
    }
}