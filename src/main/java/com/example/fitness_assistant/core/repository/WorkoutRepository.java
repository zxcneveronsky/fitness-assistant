package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.workout.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WorkoutRepository {
    Optional<Workout> findById(Long id,Long userId);
    Page<Workout> findAllByUserId(Long userId,Pageable pageable);
    Page<Workout> searchWorkout(String name,Long userId, Pageable pageable);
    Workout save(Workout workout);
    void deleteById(Long id,Long userId);
    boolean existsById(Long id, Long userId);
}
