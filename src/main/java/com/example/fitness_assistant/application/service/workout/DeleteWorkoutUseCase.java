package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteWorkoutUseCase {

    private final WorkoutRepository workoutRepository;

    @Transactional
    public void deleteWorkout(Long userId, Long workoutId) {
        long deleted = workoutRepository.deleteById(workoutId, userId);
        if (deleted == 0) {
            throw new WorkoutNotFoundException(workoutId);
        }
        log.info("Тренировка удалена | id={}", workoutId);
    }
}
