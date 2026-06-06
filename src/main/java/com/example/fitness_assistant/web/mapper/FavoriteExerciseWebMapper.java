package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.FavoriteExercise;
import com.example.fitness_assistant.web.dto.response.FavoriteExerciseResponse;
import org.springframework.stereotype.Component;

@Component
public class FavoriteExerciseWebMapper {

    public FavoriteExerciseResponse toResponse(FavoriteExercise favoriteExercise) {
        return new FavoriteExerciseResponse(
                favoriteExercise.getExerciseId()
        );
    }
}
