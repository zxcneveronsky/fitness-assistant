package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.exercise.Exercise;
import com.example.fitness_assistant.core.model.FavoriteExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteExerciseRepository {

    boolean existsByExerciseIdAndUserId(Long exerciseId, Long userId);

    FavoriteExercise save(FavoriteExercise favoriteExercise);

    void deleteByExerciseIdAndUserId(Long exerciseId, Long userId);

    Page<Exercise> searchFavoriteExercise(String name, Long muscleId, Long userId, Pageable pageable);

    List<Long> findIdsByUserId(Long userId);
}
