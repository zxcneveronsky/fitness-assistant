package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.model.workout.WorkoutWithExercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindWorkoutUseCase {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public Page<Workout> findAllByUserId(Long userId, Pageable pageable) {
        Page<Workout> workouts = workoutRepository.findAllByUserId(userId, pageable);
        log.info("Поиск тренировок завершён | userId={} | найдено={} | страница={}/{}",
                userId, workouts.getTotalElements(), workouts.getNumber() + 1, workouts.getTotalPages());
        return workouts;
    }

    @Transactional(readOnly = true)
    public Page<Workout> searchWorkout(Long userId, String name, Pageable pageable) {
        Page<Workout> workouts = workoutRepository.searchWorkout(name, userId, pageable);
        log.info("Поиск тренировок завершён | userId={} | name='{}' | найдено={} | страница={}/{}",
                userId, name, workouts.getTotalElements(), workouts.getNumber() + 1, workouts.getTotalPages());
        return workouts;
    }

    @Transactional(readOnly = true)
    public WorkoutWithExercise findById(Long userId, Long workoutId) {
        if (!workoutRepository.hasAccess(workoutId, userId)) {
            throw new WorkoutNotFoundException(workoutId);
        }
        Workout workout = workoutRepository.findByIdAccessible(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));
        List<Long> ids = workout.getExerciseIds();
        WorkoutWithExercise workoutWithExercise = new WorkoutWithExercise(
                workout.getId(),
                workout.getUserId(),
                workout.getName(),
                ids != null ? exerciseRepository.findAllByIdIn(ids) : List.of()
        );
        log.info("Тренировка найдена | id={} | userId={}", workoutId, userId);
        return workoutWithExercise;
    }
}
