package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.repository.FoodRepository;
import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExistsMealUseCase {

    private final MealRepository mealRepository;

    public boolean existsMeal(Long id, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        return mealRepository.existsById(id,userId);
    }
}