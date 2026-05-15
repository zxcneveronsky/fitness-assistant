package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.model.workout.WorkoutWithExercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class FindWorkoutUseCase {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    public Page<Workout> findWorkout(String name, UserDetails userDetails, Pageable pageable) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Page<Workout> workouts = workoutRepository.searchWorkout(name, userId, pageable);
        log.info("Поиск тренировок завершён | name='{}' | найдено={} | страница={}/{}",
                name, workouts.getTotalElements(), workouts.getNumber() + 1, workouts.getTotalPages());
        return workouts;
    }

    @Transactional
    public WorkoutWithExercise findById(Long id, UserDetails userDetails){
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Workout w = workoutRepository.findById(id, userId)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
        WorkoutWithExercise workout = new WorkoutWithExercise(
                w.getId(),
                w.getUserId(),
                w.getName(),
                exerciseRepository.findAllByIdIn(w.getExercisesIds())
        );
        log.info("Тренировка найдена | id={}", id);
        return workout;
    }
}
