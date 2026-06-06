package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateWorkoutUseCase {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Workout createWorkout(Long userId, Workout workout) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        List<Long> ids = workout.getExerciseIds();
        if (!exerciseRepository.existsAllByIdIn(ids)) {
            throw new ExerciseNotFoundException(ids.getFirst());
        }
        workout.setUserId(userId);
        Workout savedWorkout = workoutRepository.save(workout);
        log.info("Тренировка создана | id={}", savedWorkout.getId());
        return savedWorkout;
    }
}
