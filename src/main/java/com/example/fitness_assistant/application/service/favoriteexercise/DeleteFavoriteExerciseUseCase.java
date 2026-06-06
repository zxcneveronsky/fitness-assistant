package com.example.fitness_assistant.application.service.favoriteexercise;

import com.example.fitness_assistant.core.exception.FavoriteExerciseNotFoundException;
import com.example.fitness_assistant.core.repository.FavoriteExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteFavoriteExerciseUseCase {

    private final FavoriteExerciseRepository favoriteExerciseRepository;

    @Transactional
    public void deleteFavorite(Long userId, Long exerciseId) {
        if (!favoriteExerciseRepository.existsByUserIdAndExerciseId(userId, exerciseId)) {
            throw new FavoriteExerciseNotFoundException(exerciseId);
        }
        favoriteExerciseRepository.deleteByUserIdAndExerciseId(userId, exerciseId);
        log.info("Упражнение удалено из избранного | exerciseId={}", exerciseId);
    }
}
