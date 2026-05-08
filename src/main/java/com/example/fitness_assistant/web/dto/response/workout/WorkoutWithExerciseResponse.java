package com.example.fitness_assistant.web.dto.response.workout;

import com.example.fitness_assistant.web.dto.response.ExerciseResponse;

import java.util.List;

public record WorkoutWithExerciseResponse(
        Long id,
        String name,
        List<ExerciseResponse> exercises
) { }
