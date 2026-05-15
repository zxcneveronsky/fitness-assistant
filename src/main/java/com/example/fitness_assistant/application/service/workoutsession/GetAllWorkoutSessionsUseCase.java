package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllWorkoutSessionsUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;

    public Page<WorkoutSession> getAllSessions(UserDetails userDetails, Pageable pageable) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Page<WorkoutSession> sessions = workoutSessionRepository.findAllByUserId(userId, pageable);
        log.info("Поиск сессий тренировок завершён | найдено={} | страница={}/{}",
                sessions.getTotalElements(), sessions.getNumber() + 1, sessions.getTotalPages());
        return sessions;
    }
}
