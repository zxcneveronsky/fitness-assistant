package com.example.fitness_assistant.controller.view;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/health-status")
public class HealthPageController {

    @GetMapping
    public String healthPage() {
        return "health-status"; // возвращает templates/health-status.html
    }
}