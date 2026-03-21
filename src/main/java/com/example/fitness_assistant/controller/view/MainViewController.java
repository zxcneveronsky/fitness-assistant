package com.example.fitness_assistant.controller.view;

import com.example.fitness_assistant.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainViewController {
    private final ExerciseService exerciseService;
    @GetMapping("/")
    public String index() {
        return "index";
    }
}