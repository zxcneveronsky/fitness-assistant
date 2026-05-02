package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.web.dto.request.create.CreateExerciseRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateExerciseRequest;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import org.springframework.stereotype.Component;


@Component
public class ExerciseMuscleWebMapper {

    public Muscle toDomain(CreateExerciseRequest.CreateMuscleRequest request) {
        return new Muscle(
                request.id(),
                null
        );
    }

    public Muscle toDomain(UpdateExerciseRequest.UpdateMuscleRequest request) {
        return new Muscle(
                request.id(),
                null
        );
    }

    public ExerciseResponse.ExerciseMuscleResponse toResponse(Muscle exerciseMuscle) {
        return new ExerciseResponse.ExerciseMuscleResponse(
                exerciseMuscle.getId(),
                exerciseMuscle.getName()
        );
    }
}

