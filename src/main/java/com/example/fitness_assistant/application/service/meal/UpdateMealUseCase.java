package com.example.fitness_assistant.application.service.meal;


import com.example.fitness_assistant.core.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import com.example.fitness_assistant.core.model.Meal;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.core.exception.MealNotFoundException;

@Service
@RequiredArgsConstructor
public class UpdateMealUseCase {
    private final MealRepository mealRepository;

    @Transactional
    public Meal updateMeal(UserDetails userDetails,Meal mealUpdate) {
        Long id = mealUpdate.getId();
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        return mealRepository.findById(id,userId)
                .map(existingMeal -> {
                    existingMeal.setUserId(userId);
                    if (mealUpdate.getName() != null) existingMeal.setName(mealUpdate.getName());
                    if (mealUpdate.getBrands() != null) existingMeal.setBrands(mealUpdate.getBrands());
                    if (mealUpdate.getKcal() != null) existingMeal.setKcal(mealUpdate.getKcal());
                    if (mealUpdate.getProteins() != null) existingMeal.setProteins(mealUpdate.getProteins());
                    if (mealUpdate.getFats() != null) existingMeal.setFats(mealUpdate.getFats());
                    if (mealUpdate.getCarbs() != null) existingMeal.setCarbs(mealUpdate.getCarbs());
                    if (mealUpdate.getConsumedAt() != null) existingMeal.setConsumedAt(mealUpdate.getConsumedAt());

                    return mealRepository.save(existingMeal);
                })
                .orElseThrow(() -> new MealNotFoundException(id));
    }


}
