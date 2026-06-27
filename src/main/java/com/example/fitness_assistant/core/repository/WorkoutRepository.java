package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.workout.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository {
    Optional<Workout> findById(Long id, Long userId);
    Page<Workout> findAllByUserId(Long userId, Pageable pageable);
    Page<Workout> searchWorkout(Long userId, String name, Pageable pageable);
    List<Workout> findAllById(List<Long> ids);
    Workout save(Workout workout);
    void deleteById(Long id, Long userId);
    boolean existsById(Long id, Long userId);
}
