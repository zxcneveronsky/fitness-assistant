package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.WorkoutAccess;
import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;

import java.util.List;
import java.util.Optional;

public interface WorkoutAccessRepository {
    WorkoutAccess save(WorkoutAccess workoutAccess);

    List<WorkoutAccess> findByOwnerIdAndWorkoutId(Long ownerId, Long workoutId);

    List<WorkoutAccess> findAllSharedWithUserId(Long userId);

    Optional<WorkoutAccess> findById(Long id);

    Optional<WorkoutAccess> findByIdAndOwnerId(Long id, Long ownerId);

    void deleteById(Long id);

    boolean existsByOwnerIdAndSharedWithUserIdAndWorkoutId(Long ownerId, Long sharedWithUserId, Long workoutId);

    boolean existsBySharedWithUserIdAndWorkoutIdAndAccessLevel(Long userId, Long workoutId, AccessLevel accessLevel);

    boolean existsBySharedWithUserIdAndWorkoutId(Long userId, Long workoutId);
}
