package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateWorkoutSessionUseCase {
    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional
    public WorkoutSession updateWorkoutSession(Long userId, Long sessionId, LocalDateTime endTime) {
        WorkoutSession updatedSession = workoutSessionRepository.findById(sessionId, userId)
                .map(existingSession -> {
                    if (endTime != null && existingSession.getStartTime() != null && endTime.isBefore(existingSession.getStartTime())) {
                        throw new IllegalArgumentException("Время окончания не может быть раньше времени начала");
                    }
                    existingSession.setEndTime(endTime);
                    return workoutSessionRepository.save(existingSession);
                })
                .orElseThrow(() -> new WorkoutSessionNotFoundException(sessionId));
        log.info("Сессия тренировки обновлена | id={}", sessionId);
        return updatedSession;
    }
}