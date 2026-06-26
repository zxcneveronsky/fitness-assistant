package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteWorkoutSessionUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional
    public void deleteWorkoutSession(Long sessionId, Long userId) {
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        workoutSessionRepository.deleteById(sessionId, userId);
        log.info("Сессия тренировки удалена | id={}", sessionId);
    }
}
