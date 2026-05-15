package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateWorkoutSessionUseCase {
    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional
    public WorkoutSession updateSession(Long id, LocalDateTime endTime, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!workoutSessionRepository.existsById(id, userId)) {
            throw new WorkoutSessionNotFoundException(id);
        }
        WorkoutSession updatedSession = workoutSessionRepository.findById(id, userId)
                .map(session -> {
                    session.setEndTime(endTime);
                    return workoutSessionRepository.save(session);
                })
                .orElseThrow(() -> new WorkoutSessionNotFoundException(id));
        log.info("Сессия тренировки обновлена | id={}", updatedSession.getId());
        return updatedSession;
    }
}