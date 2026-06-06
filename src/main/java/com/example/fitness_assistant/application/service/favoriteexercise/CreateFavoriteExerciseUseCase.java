package com.example.fitness_assistant.application.service.favoriteexercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.FavoriteExerciseAlreadyExistsException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.FavoriteExercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.FavoriteExerciseRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateFavoriteExerciseUseCase {

    private final FavoriteExerciseRepository favoriteExerciseRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public FavoriteExercise createFavorite(Long userId, Long exerciseId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new ExerciseNotFoundException(exerciseId);
        }
        if (favoriteExerciseRepository.existsByUserIdAndExerciseId(userId, exerciseId)) {
            throw new FavoriteExerciseAlreadyExistsException(exerciseId);
        }
        FavoriteExercise savedFavoriteExercise = favoriteExerciseRepository.save(
                new FavoriteExercise(null, userId, exerciseId)
        );
        log.info("Упражнение добавлено в избранное | exerciseId={}", exerciseId);
        return savedFavoriteExercise;
    }
}
