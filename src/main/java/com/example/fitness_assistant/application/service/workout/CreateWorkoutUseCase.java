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
        List<Long> ids = workout.getExerciseIds().stream().distinct().toList();
        if (!exerciseRepository.existsAllByIdIn(ids)) {
            throw new ExerciseNotFoundException(ids.getFirst());
        }
        Workout newWorkout = new Workout(
                null,
                userId,
                workout.getName(),
                workout.getExerciseIds()
        );
        Workout savedWorkout = workoutRepository.save(newWorkout);
        log.info("Тренировка создана | id={} | название='{}'", savedWorkout.getId(), savedWorkout.getName());
        return savedWorkout;
    }
}
