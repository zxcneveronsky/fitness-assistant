package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.AccessDeniedException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutAccessRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateWorkoutSessionUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutAccessRepository workoutAccessRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkoutSession createWorkoutSession(Long userId, Long workoutId, LocalDateTime startTime) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        boolean isOwner = workoutRepository.existsById(workoutId, userId);
        boolean hasAccess = workoutAccessRepository.existsByWorkoutIdAndSharedWithUserId(workoutId, userId);
        if (!isOwner && !hasAccess) {
            throw new AccessDeniedException();
        }
        WorkoutSession savedSession = workoutSessionRepository.save(
                new WorkoutSession(
                    null,
                    workoutId,
                    userId,
                    startTime,
                    null
                )
        );
        log.info("Сессия тренировки создана | id={} | userId={} | workoutId={}", savedSession.getId(), userId, workoutId);
        return savedSession;
    }
}
