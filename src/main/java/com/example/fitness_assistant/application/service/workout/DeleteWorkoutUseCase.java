package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.HydrationNotFoundException;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteWorkoutUseCase {

    private final WorkoutRepository workoutRepository;

    @Transactional
    public void deleteWorkout(Long id, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!workoutRepository.existsById(id, userId)) {
            throw new HydrationNotFoundException(id);
        }
        workoutRepository.deleteById(id, userId);
    }
}
