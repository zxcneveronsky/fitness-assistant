package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.FavoriteFood;
import com.example.fitness_assistant.core.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteFoodRepository {

    boolean existsByFoodIdAndUserId(Long foodId, Long userId);

    FavoriteFood save(FavoriteFood favoriteFood);

    void deleteByFoodIdAndUserId(Long foodId, Long userId);

    Page<Food> searchFavoriteFood(String name, Long userId, Pageable pageable);

    List<Long> findIdsByUserId(Long userId);
}
