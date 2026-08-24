package com.example.fitness_assistant.application.service.favoriteexercise;

import com.example.fitness_assistant.core.repository.FavoriteExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetFavoriteExerciseIdsUseCase {

    private final FavoriteExerciseRepository favoriteExerciseRepository;

    @Transactional(readOnly = true)
    public List<Long> getFavoriteExerciseIds(Long userId) {
        List<Long> ids = favoriteExerciseRepository.findIdsByUserId(userId);
        log.info("Получены id избранных упражнений | userId={} | count={}", userId, ids.size());
        return ids;
    }
}
