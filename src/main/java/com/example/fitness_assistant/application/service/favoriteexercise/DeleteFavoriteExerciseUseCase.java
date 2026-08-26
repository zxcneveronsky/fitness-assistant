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
    public void deleteFavoriteExercise(Long userId, Long exerciseId) {
        long deleted = favoriteExerciseRepository.deleteByExerciseIdAndUserId(exerciseId, userId);
        if (deleted == 0) {
            throw new FavoriteExerciseNotFoundException(exerciseId);
        }
        log.info("Упражнение удалено из избранного | exerciseId={}", exerciseId);
    }
}
