package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindWorkoutSessionUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;

    public WorkoutSession findById(Long id, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        WorkoutSession session = workoutSessionRepository.findById(id, userId)
                .orElseThrow(() -> new WorkoutSessionNotFoundException(id));
        log.info("Сессия тренировки найдена | id={}", id);
        return session;
    }
}
