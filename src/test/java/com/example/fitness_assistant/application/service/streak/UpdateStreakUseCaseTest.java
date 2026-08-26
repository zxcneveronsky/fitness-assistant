package com.example.fitness_assistant.application.service.streak;

import com.example.fitness_assistant.core.model.Streak;
import com.example.fitness_assistant.core.repository.StreakRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateStreakUseCaseTest {
    @Mock
    StreakRepository streakRepository;
    @InjectMocks
    UpdateStreakUseCase updateStreakUseCase;
    @Test
    public void updateStreakTest(){
        LocalDate today = LocalDate.now();

        Streak testStreak = new Streak(1L, 0, today.minusDays(1));
        Streak testStreak0 = new Streak(2L, 1, today.minusDays(2));

        when(streakRepository.findById(1L)).thenReturn(Optional.of(testStreak));
        when(streakRepository.findById(2L)).thenReturn(Optional.of(testStreak0));

        when(streakRepository.save(testStreak)).thenReturn(testStreak);
        when(streakRepository.save(testStreak0)).thenReturn(testStreak0);

        Streak testSavedStreak = updateStreakUseCase.updateStreak(1L, today);
        Streak testSavedStreak0 = updateStreakUseCase.updateStreak(2L, today);

        assertEquals(1,testSavedStreak.getStreak());
        assertEquals(0,testSavedStreak0.getStreak());

    }
}
