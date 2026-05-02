package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.MealNotFoundException;

import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMealUseCase {
    private final MealRepository mealRepository;

    @Transactional
    public void deleteById(Long id,UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();

        if (!mealRepository.existsById(id,userId)) {
            throw new MealNotFoundException(id);
        }
        mealRepository.deleteById(id,userId);
    }
}
