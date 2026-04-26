package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.web.dto.request.CreateExerciseRequest;
import com.example.fitness_assistant.web.dto.request.UpdateExerciseRequest;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ExerciseWebMapper {

    private final ExerciseMuscleWebMapper exerciseMuscleWebMapper;

    public Exercise toDomain(CreateExerciseRequest request) {
        return new Exercise(
                null,
                request.exerciseName(),
                request.description(),
                request.muscles().stream()
                        .map(exerciseMuscleWebMapper::toDomain)
                        .toList()
        );
    }

    public Exercise toDomain(UpdateExerciseRequest request) {
        return new Exercise(
                request.id(),
                request.exerciseName(),
                request.description(),
                request.muscles().stream()
                        .map(exerciseMuscleWebMapper::toDomain)
                        .toList()
        );
    }

    public ExerciseResponse toResponse(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getName(),
                exercise.getDescription(),
                exercise.getMuscles().stream()
                        .map(exerciseMuscleWebMapper::toResponse)
                        .toList()
        );
    }
}
