package com.example.fitness_assistant.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/explore")
public class ExerciseViewController {
    public ExerciseViewController() {}
    @GetMapping("/exercises")
    public String showExercises() {
        return "explore-exercises";
    }
}