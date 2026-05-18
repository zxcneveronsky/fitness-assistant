package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindWorkoutSessionUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional(readOnly = true)
    public WorkoutSession findById(Long id, Long userId) {
        WorkoutSession session = workoutSessionRepository.findById(id, userId)
                .orElseThrow(() -> new WorkoutSessionNotFoundException(id));
        log.info("Сессия тренировки найдена | id={}", id);
        return session;
    }
}
