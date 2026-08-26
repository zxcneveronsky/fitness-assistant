package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;
import com.example.fitness_assistant.core.model.workout.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository {
    Optional<Workout> findById(Long id, Long userId);
    Optional<Workout> findAccessibleById(Long id, Long userId);
    Optional<Workout> findAccessibleByIdWithLevel(Long id, Long userId, AccessLevel accessLevel);
    Page<Workout> findAllByUserId(Long userId, Pageable pageable);
    Page<Workout> searchWorkout(String name, Long userId, Pageable pageable);
    List<Workout> findAllById(List<Long> ids);
    boolean hasAccess(Long workoutId, Long userId);
    List<Workout> findAllAccessibleByIdIn(List<Long> ids, Long userId);
    Workout save(Workout workout);
    void deleteById(Long id, Long userId);
    boolean existsById(Long id, Long userId);
}
