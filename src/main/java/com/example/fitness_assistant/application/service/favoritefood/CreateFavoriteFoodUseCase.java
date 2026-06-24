package com.example.fitness_assistant.application.service.favoritefood;

import com.example.fitness_assistant.core.exception.FavoriteFoodAlreadyExistsException;
import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.FavoriteFood;
import com.example.fitness_assistant.core.repository.FavoriteFoodRepository;
import com.example.fitness_assistant.core.repository.FoodRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateFavoriteFoodUseCase {

    private final FavoriteFoodRepository favoriteFoodRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public FavoriteFood createFavoriteFood(Long userId, Long foodId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!foodRepository.existsById(foodId)) {
            throw new FoodNotFoundException(foodId);
        }
        if (favoriteFoodRepository.existsByUserIdAndFoodId(userId, foodId)) {
            throw new FavoriteFoodAlreadyExistsException(foodId);
        }
        FavoriteFood savedFavoriteFood = favoriteFoodRepository.save(
                new FavoriteFood(null, userId, foodId)
        );
        log.info("Продукт добавлен в избранное | foodId={}", foodId);
        return savedFavoriteFood;
    }
}
