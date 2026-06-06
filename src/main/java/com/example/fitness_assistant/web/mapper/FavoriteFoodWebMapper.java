package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.FavoriteFood;
import com.example.fitness_assistant.web.dto.response.FavoriteFoodResponse;
import org.springframework.stereotype.Component;

@Component
public class FavoriteFoodWebMapper {

    public FavoriteFoodResponse toResponse(FavoriteFood favoriteFood) {
        return new FavoriteFoodResponse(
                favoriteFood.getFoodId()
        );
    }
}
