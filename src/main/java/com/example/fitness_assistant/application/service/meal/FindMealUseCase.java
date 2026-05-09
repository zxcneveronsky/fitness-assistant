package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.MealNotFoundException;
import com.example.fitness_assistant.core.model.meal.Meal;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.fitness_assistant.core.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FindMealUseCase {
    private final MealRepository mealRepository;

    public Page<Meal> findMeal(LocalDateTime localDateTime, UserDetails userDetails, Pageable pageable){
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        return mealRepository.searchMeal(localDateTime,userId,pageable);
    }

    @Transactional
    public Meal findById(Long id, UserDetails userDetails){
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        return mealRepository.findById(id,userId).orElseThrow(()->new MealNotFoundException(id));
    }
}
