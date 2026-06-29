package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.model.Targets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TargetCalculationServiceTest {
    TargetCalculationService targetCalculationService = new TargetCalculationService();
    Targets testTargets = new Targets(1L, 2400D, null, null, null, null, false);
    @Test
    public void balanceMacrosByCaloriesTest() {
        targetCalculationService.balanceMacrosByCalories(testTargets);
        assertEquals(180D,testTargets.getTargetProteins());
        assertEquals(80D,testTargets.getTargetFats());
        assertEquals(240D,testTargets.getTargetCarbs());
    }
}
