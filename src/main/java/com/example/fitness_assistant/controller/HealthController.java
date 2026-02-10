package com.example.fitness_assistant.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("api/health")
public class HealthController {
    @GetMapping
    public Map<String,Object> GetStatusHealth(){
        return Map.of(
                "status","UP",
                "message","Фитнес Ассистент готов к работе.",
                "timestamp", LocalDateTime.now().toString()
        );
    }
}
