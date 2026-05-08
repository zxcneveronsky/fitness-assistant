package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteWorkoutUseCase {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    @Transactional
    public void deleteWorkout(Long id, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!workoutRepository.existsById(id, userId)) {
            throw new WorkoutNotFoundException(id);
        }
        workoutRepository.deleteById(id, userId);
    }
}
