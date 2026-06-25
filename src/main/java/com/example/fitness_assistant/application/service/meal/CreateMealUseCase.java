package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.meal.Meal;
import com.example.fitness_assistant.core.repository.FoodRepository;
import java.time.LocalDateTime;
import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateMealUseCase {
    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @Transactional
    public Meal createMealManual(Long userId, Meal meal) {
        meal.setId(null);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        meal.setUserId(userId);
        Meal savedMeal = mealRepository.save(meal);
        log.info("Приём пищи создан | id={} | название='{}'", savedMeal.getId(), savedMeal.getName());
        return savedMeal;
    }

    @Transactional
    public Meal createMealAuto(Long userId, Long foodId, Double weight, LocalDateTime consumedAt) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (weight == null) {
            throw new IllegalArgumentException("Вес не может быть null");
        }
        Double k = weight / 100.0;
        Meal savedMeal = foodRepository.findById(foodId).map(
                food -> {
                    Meal newMeal = new Meal(
                            null,
                            userId,
                            food.getName(),
                            food.getBrands(),
                            food.getKcal() * k,
                            food.getProteins() * k,
                            food.getFats() * k,
                            food.getCarbs() * k,
                            consumedAt
                    );
                    return mealRepository.save(newMeal);
                }
        ).orElseThrow(() -> new FoodNotFoundException(foodId));
        log.info("Приём пищи создан | id={} | название='{}'", savedMeal.getId(), savedMeal.getName());
        return savedMeal;
    }

}
