package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.muscle.FindMuscleUseCase;
import com.example.fitness_assistant.web.dto.response.MuscleResponse;
import com.example.fitness_assistant.web.mapper.MuscleWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/muscle")
@RequiredArgsConstructor
@Validated
public class MuscleController {

    private final FindMuscleUseCase findMuscleUseCase;
    private final MuscleWebMapper muscleWebMapper;

    @GetMapping("/search")
    public List<MuscleResponse> searchMuscles(@RequestParam(required = false) String name) {
        return findMuscleUseCase.searchMuscles(name).stream()
                .map(muscleWebMapper::toResponse)
                .toList();
    }
}
