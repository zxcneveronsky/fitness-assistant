package com.example.fitness_assistant.application.service.favoritefood;

import com.example.fitness_assistant.core.exception.FavoriteFoodNotFoundException;
import com.example.fitness_assistant.core.repository.FavoriteFoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteFavoriteFoodUseCase {

    private final FavoriteFoodRepository favoriteFoodRepository;

    @Transactional
    public void deleteFavoriteFood(Long userId, Long foodId) {
        if (!favoriteFoodRepository.existsByUserIdAndFoodId(userId, foodId)) {
            throw new FavoriteFoodNotFoundException(foodId);
        }
        favoriteFoodRepository.deleteByUserIdAndFoodId(userId, foodId);
        log.info("Продукт удалён из избранного | foodId={}", foodId);
    }
}
