package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteWorkoutSessionUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional
    public void deleteSession(Long id, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!workoutSessionRepository.existsById(id, userId)) {
            throw new WorkoutSessionNotFoundException(id);
        }
        workoutSessionRepository.deleteById(id, userId);
        log.info("Сессия тренировки удалена | id={}", id);
    }
}
