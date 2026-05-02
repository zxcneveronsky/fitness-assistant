package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Meal;
import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.infrastructure.mapper.MealMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.MealEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaMealRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MealRepositoryAdapter implements MealRepository {

    private final JpaMealRepository jpaMealRepository;
    private final JpaUserRepository jpaUserRepository;
    private final MealMapper mealMapper;

    @Override
    public Meal save(Meal meal) {
        Long userId = meal.getUserId();
        MealEntity mealEntity = mealMapper.toEntity(meal);
        mealEntity.setUser(jpaUserRepository.getReferenceById(userId));
        return mealMapper.toDomain(jpaMealRepository.save(mealEntity));
    }

    @Override
    public Page<Meal> searchMeal(LocalDate localDate, Long userId, Pageable pageable) {
        return jpaMealRepository.searchMeal(userId, localDate, pageable)
                .map(mealMapper::toDomain);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        jpaMealRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsById(Long id, Long userId) {
        return jpaMealRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public Optional<Meal> findById(Long id, Long userId) {
        return jpaMealRepository.findByIdAndUserId(id, userId)
                .map(mealMapper::toDomain);
    }
}
