package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.web.dto.request.create.CreateExerciseRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateExerciseRequest;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ExerciseWebMapper {

    private final ExerciseMuscleWebMapper exerciseMuscleWebMapper;

    public Exercise toDomain(CreateExerciseRequest request) {
        return new Exercise(
                null,
                request.exerciseName(),
                request.description(),
                request.musclesId() != null
                        ? request.musclesId().stream().map(exerciseMuscleWebMapper::toDomain).toList()
                        : List.of()
        );
    }

    public Exercise toDomain(UpdateExerciseRequest request) {
        return new Exercise(
                request.id(),
                request.exerciseName(),
                request.description(),
                request.musclesId() != null
                        ? request.musclesId().stream().map(exerciseMuscleWebMapper::toDomain).toList()
                        : List.of()
        );
    }

    public ExerciseResponse toResponse(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getMuscles() != null
                        ? exercise.getMuscles().stream().map(exerciseMuscleWebMapper::toResponse).toList()
                        : List.of()
        );
    }
}
