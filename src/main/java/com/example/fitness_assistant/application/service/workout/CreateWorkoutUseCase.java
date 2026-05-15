package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
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
public class CreateWorkoutUseCase {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Transactional
    public Workout createWorkout(UserDetails userDetails, Workout workout) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        workout.setUserId(userId);
        Workout savedWorkout = workoutRepository.save(workout);
        log.info("Тренировка создана | id={}", savedWorkout.getId());
        return savedWorkout;
    }
}
