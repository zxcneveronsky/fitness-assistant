package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateWorkoutSessionUseCaseTest {
    @Mock
    WorkoutSessionRepository workoutSessionRepository;
    @InjectMocks
    UpdateWorkoutSessionUseCase updateWorkoutSessionUseCase;
    @Test
    public void updateWorkoutSessionTest(){
        LocalDateTime today = LocalDateTime.now();

        WorkoutSession testWorkoutSession = new WorkoutSession(1L,1L,1L,today,null);

        when(workoutSessionRepository.findById(1L,1L)).thenReturn(Optional.of(testWorkoutSession));

        assertThrows(IllegalArgumentException.class,() -> updateWorkoutSessionUseCase.updateWorkoutSession(1L,1L,today.minusDays(1)));

    }
}
