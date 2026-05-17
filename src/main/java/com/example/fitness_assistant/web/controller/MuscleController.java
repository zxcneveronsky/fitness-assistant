package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.exercise.GetAllMusclesUseCase;
import com.example.fitness_assistant.web.dto.response.MuscleResponse;
import com.example.fitness_assistant.web.mapper.MuscleWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/muscles")
@RequiredArgsConstructor
public class MuscleController {

    private final GetAllMusclesUseCase getAllMusclesUseCase;
    private final MuscleWebMapper muscleWebMapper;

    @GetMapping
    public List<MuscleResponse> getAllMuscles() {
        return getAllMusclesUseCase.getAllMuscles().stream()
                .map(muscleWebMapper::toResponse)
                .toList();
    }
}
