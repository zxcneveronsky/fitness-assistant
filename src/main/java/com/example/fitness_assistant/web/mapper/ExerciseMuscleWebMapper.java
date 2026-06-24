package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import org.springframework.stereotype.Component;


@Component
public class ExerciseMuscleWebMapper {

    public ExerciseResponse.ExerciseMuscleResponse toResponse(Muscle exerciseMuscle) {
        return new ExerciseResponse.ExerciseMuscleResponse(
                exerciseMuscle.getId(),
                exerciseMuscle.getName()
        );
    }
}
