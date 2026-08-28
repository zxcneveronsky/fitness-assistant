package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaFoodRepository;
import com.example.fitness_assistant.infrastructure.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FoodRepositoryAdapter implements FoodRepository {

    private final JpaFoodRepository jpaFoodRepository;
    private final FoodMapper foodMapper;

    @Override
    @Cacheable("food")
    public Optional<Food> findById(Long id) {
        return jpaFoodRepository.findById(id)
                .map(foodMapper::toDomain);
    }

    @Override
    public Page<Food> searchFood(String name, Pageable pageable) {
        Page<FoodEntity> page = (name == null || name.isBlank())
                ? jpaFoodRepository.findAllByOrderByNameAsc(pageable)
                : jpaFoodRepository.searchByName(name.trim(), pageable);
        return page.map(foodMapper::toDomain);
    }

    @Override
    @CacheEvict(value = "food", allEntries = true)
    public Food save(Food food) {
        return foodMapper.toDomain(jpaFoodRepository.save(foodMapper.toEntity(food)));
    }

    @Override
    @CacheEvict(value = "food", allEntries = true)
    public void deleteById(Long id) {
        jpaFoodRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaFoodRepository.existsById(id);
    }
}