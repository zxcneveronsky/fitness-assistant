package com.example.fitness_assistant.application.service.favoriteexercise;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.FavoriteExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindFavoriteExerciseUseCase {

    private final FavoriteExerciseRepository favoriteExerciseRepository;

    @Transactional(readOnly = true)
    public Page<Exercise> searchFavoriteExercise(Long userId, String name, Long muscleId, Pageable pageable) {
        Page<Exercise> exercises = favoriteExerciseRepository.searchFavoriteExercise(userId, name, muscleId, pageable);
        log.info("Поиск избранных упражнений завершён | userId={} | name='{}' | muscleId={} | найдено={}", userId, name, muscleId, exercises.getTotalElements());
        return exercises;
    }
}
