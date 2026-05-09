package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.meal.DailyNutrition;
import com.example.fitness_assistant.core.model.meal.Meal;
import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.infrastructure.mapper.DailyNutritionMapper;
import com.example.fitness_assistant.infrastructure.mapper.MealMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.MealEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaMealRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MealRepositoryAdapter implements MealRepository {

    private final JpaMealRepository jpaMealRepository;
    private final JpaUserRepository jpaUserRepository;
    private final MealMapper mealMapper;
    private final DailyNutritionMapper dailyNutritionMapper;

    @Override
    public Meal save(Meal meal) {
        Long userId = meal.getUserId();
        MealEntity mealEntity = mealMapper.toEntity(meal);
        mealEntity.setUser(jpaUserRepository.getReferenceById(userId));
        return mealMapper.toDomain(jpaMealRepository.save(mealEntity));
    }

    @Override
    public Page<Meal> searchMeal(LocalDateTime localDateTime, Long userId, Pageable pageable) {
        LocalDateTime startOfDay = localDateTime != null ? localDateTime.toLocalDate().atStartOfDay() : null;
        LocalDateTime endOfDay = localDateTime != null ? localDateTime.toLocalDate().atTime(LocalTime.MAX) : null;
        return jpaMealRepository.searchMeal(userId, startOfDay, endOfDay, pageable)
                .map(mealMapper::toDomain);
    }
    @Override
    public DailyNutrition getDailyNutrition(LocalDateTime localDateTime, Long userId) {
        LocalDateTime startOfDay = localDateTime != null ? localDateTime.toLocalDate().atStartOfDay() : null;
        LocalDateTime endOfDay = localDateTime != null ? localDateTime.toLocalDate().atTime(LocalTime.MAX) : null;
        return dailyNutritionMapper.toDomain(jpaMealRepository.getDailyNutrition(userId, startOfDay, endOfDay));
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
