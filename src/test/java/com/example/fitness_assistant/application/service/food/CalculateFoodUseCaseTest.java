package com.example.fitness_assistant.application.service.food;

import com.example.fitness_assistant.core.model.Food;
import com.example.fitness_assistant.core.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculateFoodUseCaseTest {
    @Mock
    FoodRepository foodRepository;
    @InjectMocks
    CalculateFoodUseCase calculateFoodUseCase;
    @Test
    public void calculateFoodTest(){
        Food testFood = new Food(1L,"Pizza","null",100D,10D,20D,10D);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(testFood));

        Food testCalculatedFood = calculateFoodUseCase.calculateFood(1L,200D);

        assertEquals(200D,testCalculatedFood.getKcal());
        assertEquals(20D,testCalculatedFood.getProteins());
        assertEquals(40D,testCalculatedFood.getFats());
        assertEquals(20D,testCalculatedFood.getCarbs());
    };
}
