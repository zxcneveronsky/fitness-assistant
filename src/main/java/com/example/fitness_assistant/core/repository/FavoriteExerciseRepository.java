package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.model.FavoriteExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteExerciseRepository {

    boolean existsByUserIdAndExerciseId(Long userId, Long exerciseId);

    FavoriteExercise save(FavoriteExercise favoriteExercise);

    void deleteByUserIdAndExerciseId(Long userId, Long exerciseId);

    Page<Exercise> searchFavoriteExercise(Long userId, String name, Long muscleId, Pageable pageable);

    List<Long> findIdsByUserId(Long userId);
}
