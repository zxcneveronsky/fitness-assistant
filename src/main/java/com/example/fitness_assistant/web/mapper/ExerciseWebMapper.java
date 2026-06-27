package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.exercise.Exercise;
import com.example.fitness_assistant.core.model.Muscle;
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
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name(),
                request.description(),
                request.muscleIds().stream().map(id -> new Muscle(id, null)).toList()
        );
    }

    public Exercise toDomain(UpdateExerciseRequest request) {
        return new Exercise(
                request.id(),
                request.name(),
                request.description(),
                request.muscleIds() != null
                        ? request.muscleIds().stream().map(id -> new Muscle(id, null)).toList()
                        : null
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
