package com.example.fitness_assistant.controller;


import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.service.ExerciseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExerciseController {
    private final ExerciseService exerciseService;
    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }
    @GetMapping
    public List<ExerciseDTO> getAllExercise(){
        return exerciseService.getAllExercise();
    }


    @GetMapping("/muscle/{muscle}")
    public List<ExerciseDTO> getExercise(@PathVariable String muscle){
        return exerciseService.getExerciseName(muscle);
    }

    @GetMapping("/exercise/{exercise}")
    public ExerciseDTO getMuscle(@PathVariable String exercise){
        return exerciseService.getMuscleName(exercise);
    }


}
