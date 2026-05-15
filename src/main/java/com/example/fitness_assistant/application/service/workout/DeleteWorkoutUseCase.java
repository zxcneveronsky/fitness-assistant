package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Тренировка удалена | id={}", id);
    }
}
