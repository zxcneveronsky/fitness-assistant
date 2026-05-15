package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.meal.DailyNutrition;
import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetDailyNutritionUseCase {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    public DailyNutrition getDailyNutrition(LocalDateTime localDateTime, UserDetails userDetails){
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        DailyNutrition nutrition = mealRepository.getDailyNutrition(localDateTime,userId);
        log.info("Дневная норма питания получена | userId={}", userId);
        return nutrition;
    }
}
