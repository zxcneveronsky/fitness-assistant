package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateWorkoutSessionUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkoutSession createSession(Long workoutId, LocalDateTime startTime, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!workoutRepository.existsById(workoutId, userId)) {
            throw new WorkoutNotFoundException(workoutId);
        }
        return workoutSessionRepository.save(
                new WorkoutSession(
                    null,
                    workoutId,
                    userId,
                    startTime,
                    null
                )
        );

    }
}
