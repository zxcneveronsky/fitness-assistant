package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.FavoriteFood;
import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FavoriteFoodRepository;
import com.example.fitness_assistant.infrastructure.mapper.FavoriteFoodMapper;
import com.example.fitness_assistant.infrastructure.mapper.FoodMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteFoodEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaFavoriteFoodRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaFoodRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FavoriteFoodRepositoryAdapter implements FavoriteFoodRepository {

    private final JpaFavoriteFoodRepository jpaFavoriteFoodRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaFoodRepository jpaFoodRepository;
    private final FavoriteFoodMapper favoriteFoodMapper;
    private final FoodMapper foodMapper;

    @Override
    public boolean existsByUserIdAndFoodId(Long userId, Long foodId) {
        return jpaFavoriteFoodRepository.existsByUserIdAndFoodId(userId, foodId);
    }

    @Override
    public FavoriteFood save(FavoriteFood favoriteFood) {
        FavoriteFoodEntity favoriteFoodEntity = favoriteFoodMapper.toEntity(favoriteFood);
        favoriteFoodEntity.setUser(jpaUserRepository.getReferenceById(favoriteFood.getUserId()));
        favoriteFoodEntity.setFood(jpaFoodRepository.getReferenceById(favoriteFood.getFoodId()));
        return favoriteFoodMapper.toDomain(jpaFavoriteFoodRepository.save(favoriteFoodEntity));
    }

    @Override
    public void deleteByUserIdAndFoodId(Long userId, Long foodId) {
        jpaFavoriteFoodRepository.deleteByUserIdAndFoodId(userId, foodId);
    }

    @Override
    public Page<Food> searchFavorites(Long userId, String name, Pageable pageable) {
        return jpaFavoriteFoodRepository.searchFavoriteFoods(userId, name, pageable)
                .map(foodMapper::toDomain);
    }

    @Override
    public List<Long> findIdsByUserId(Long userId) {
        return jpaFavoriteFoodRepository.findIdsByUserId(userId);
    }
}
