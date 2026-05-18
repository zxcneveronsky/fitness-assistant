package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllWorkoutSessionsUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional(readOnly = true)
    public Page<WorkoutSession> getAllSessions(Long userId, Pageable pageable) {
        Page<WorkoutSession> sessions = workoutSessionRepository.findAllByUserId(userId, pageable);
        log.info("Поиск сессий тренировок завершён | найдено={} | страница={}/{}",
                sessions.getTotalElements(), sessions.getNumber() + 1, sessions.getTotalPages());
        return sessions;
    }
}
