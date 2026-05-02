package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.Meal;
import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMealUseCase {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @Transactional
    public Meal createMeal(UserDetails userDetails, Meal meal) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        meal.setUserId(userId);
        return mealRepository.save(meal);
    }
}
