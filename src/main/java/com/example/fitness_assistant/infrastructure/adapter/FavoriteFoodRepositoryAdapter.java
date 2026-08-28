package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.FavoriteFood;
import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FavoriteFoodRepository;
import com.example.fitness_assistant.infrastructure.mapper.FavoriteFoodMapper;
import com.example.fitness_assistant.infrastructure.mapper.FoodMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteFoodEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
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
    public boolean existsByFoodIdAndUserId(Long foodId, Long userId) {
        return jpaFavoriteFoodRepository.existsByFoodIdAndUserId(foodId, userId);
    }

    @Override
    public FavoriteFood save(FavoriteFood favoriteFood) {
        FavoriteFoodEntity favoriteFoodEntity = favoriteFoodMapper.toEntity(favoriteFood);
        favoriteFoodEntity.setUser(jpaUserRepository.getReferenceById(favoriteFood.getUserId()));
        favoriteFoodEntity.setFood(jpaFoodRepository.getReferenceById(favoriteFood.getFoodId()));
        return favoriteFoodMapper.toDomain(jpaFavoriteFoodRepository.save(favoriteFoodEntity));
    }

    @Override
    public long deleteByFoodIdAndUserId(Long foodId, Long userId) {
        return jpaFavoriteFoodRepository.deleteByFoodIdAndUserId(foodId, userId);
    }

    @Override
    public Page<Food> searchFavoriteFood(String name, Long userId, Pageable pageable) {
        Page<FoodEntity> page = (name == null || name.isBlank())
                ? jpaFavoriteFoodRepository.findByUserIdOrderByNameAsc(userId, pageable)
                : jpaFavoriteFoodRepository.searchByNameAndUserId(name.trim(), userId, pageable);
        return page.map(foodMapper::toDomain);
    }

    @Override
    public List<Long> findIdsByUserId(Long userId) {
        return jpaFavoriteFoodRepository.findIdsByUserId(userId);
    }
}
