package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.FavoriteFood;
import com.example.fitness_assistant.core.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteFoodRepository {

    boolean existsByUserIdAndFoodId(Long userId, Long foodId);

    FavoriteFood save(FavoriteFood favoriteFood);

    void deleteByUserIdAndFoodId(Long userId, Long foodId);

    Page<Food> searchFavoriteFood(Long userId, String name, Pageable pageable);

    List<Long> findIdsByUserId(Long userId);
}
