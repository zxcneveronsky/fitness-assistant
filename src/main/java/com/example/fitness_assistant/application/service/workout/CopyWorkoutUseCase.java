package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CopyWorkoutUseCase {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Transactional
    public Workout copyWorkout(Long userId, Long workoutId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Workout existingWorkout = workoutRepository
                .findAccessibleByIdWithLevel(workoutId, userId, AccessLevel.COPY)
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));
        Workout copy = new Workout(
                null,
                userId,
                "Копия - " + existingWorkout.getName(),
                existingWorkout.getExerciseIds()
        );
        Workout savedWorkout = workoutRepository.save(copy);
        log.info("Тренировка скопирована | id={} | название='{}' | из workoutId={}",
                savedWorkout.getId(), savedWorkout.getName(), workoutId);
        return savedWorkout;
    }
}
