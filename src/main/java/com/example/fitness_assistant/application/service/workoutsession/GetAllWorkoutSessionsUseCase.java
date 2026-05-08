package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllWorkoutSessionsUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;

    public Page<WorkoutSession> getAllSessions(UserDetails userDetails, Pageable pageable) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        return workoutSessionRepository.findAllByUserId(userId, pageable);
    }
}
