package com.example.fitness_assistant.application.service.favoritefood;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FavoriteFoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindFavoriteFoodUseCase {

    private final FavoriteFoodRepository favoriteFoodRepository;

    @Transactional(readOnly = true)
    public Page<Food> searchFavoriteFood(Long userId, String name, Pageable pageable) {
        Page<Food> foods = favoriteFoodRepository.searchFavoriteFood(name, userId, pageable);
        log.info("Поиск избранных продуктов завершён | userId={} | name='{}' | найдено={}", userId, name, foods.getTotalElements());
        return foods;
    }
}
