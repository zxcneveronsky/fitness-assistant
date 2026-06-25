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
    public WorkoutSession updateWorkoutSession(Long userId, Long id, LocalDateTime endTime) {
        WorkoutSession updatedSession = workoutSessionRepository.findById(id, userId)
                .map(session -> {
                    if (endTime != null && session.getStartTime() != null && endTime.isBefore(session.getStartTime())) {
                        throw new IllegalArgumentException("Время окончания не может быть раньше времени начала");
                    }
                    session.setEndTime(endTime);
                    return workoutSessionRepository.save(session);
                })
                .orElseThrow(() -> new WorkoutSessionNotFoundException(id));
        log.info("Сессия тренировки обновлена | id={}", updatedSession.getId());
        return updatedSession;
    }
}