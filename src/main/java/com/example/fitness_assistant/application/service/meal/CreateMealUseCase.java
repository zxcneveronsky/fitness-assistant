package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.FoodNotFoundException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.meal.Meal;
import com.example.fitness_assistant.core.repository.FoodRepository;
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
    public Meal createMealAuto(Long userId, Long id, Double weight, Meal meal) {
        meal.setId(null);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (weight == null) {
            throw new IllegalArgumentException("Вес не может быть null");
        }
        Double k = weight / 100.0;
        Meal savedMeal =  foodRepository.findById(id).map(
                food ->{
                    meal.setUserId(userId);
                    meal.setName(food.getName());
                    meal.setBrands(food.getBrands());
                    meal.setKcal(food.getKcal()*k);
                    meal.setProteins(food.getProteins()*k);
                    meal.setFats(food.getFats()*k);
                    meal.setCarbs(food.getCarbs()*k);
                    return mealRepository.save(meal);
                }
        ).orElseThrow(()->new FoodNotFoundException(id));
        log.info("Приём пищи создан | id={} | название='{}'", savedMeal.getId(), savedMeal.getName());
        return savedMeal;

    }

}
