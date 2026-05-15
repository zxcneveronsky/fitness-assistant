package com.example.fitness_assistant.application.service.meal;

import com.example.fitness_assistant.core.exception.MealNotFoundException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.meal.Meal;
import com.example.fitness_assistant.core.repository.MealRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMealUseCase {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    @Transactional
    public Meal updateMeal(UserDetails userDetails, Meal mealUpdate) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Long mealId = mealUpdate.getId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Meal updatedMeal = mealRepository.findById(mealId, userId)
                .map(existingMeal -> {
                    existingMeal.setName(mealUpdate.getName() != null ? mealUpdate.getName() : existingMeal.getName());
                    existingMeal.setBrands(mealUpdate.getBrands() != null ? mealUpdate.getBrands() : existingMeal.getBrands());
                    existingMeal.setKcal(mealUpdate.getKcal() != null ? mealUpdate.getKcal() : existingMeal.getKcal());
                    existingMeal.setProteins(mealUpdate.getProteins() != null ? mealUpdate.getProteins() : existingMeal.getProteins());
                    existingMeal.setFats(mealUpdate.getFats() != null ? mealUpdate.getFats() : existingMeal.getFats());
                    existingMeal.setCarbs(mealUpdate.getCarbs() != null ? mealUpdate.getCarbs() : existingMeal.getCarbs());
                    existingMeal.setConsumedAt(mealUpdate.getConsumedAt() != null ? mealUpdate.getConsumedAt() : existingMeal.getConsumedAt());
                    return mealRepository.save(existingMeal);
                })
                .orElseThrow(() -> new MealNotFoundException(mealId));
        log.info("Приём пищи обновлён | id={}", updatedMeal.getId());
        return updatedMeal;
    }
}
