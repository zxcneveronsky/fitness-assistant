package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.WorkoutAccess;
import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;

import java.util.List;
import java.util.Optional;

public interface WorkoutAccessRepository {
    WorkoutAccess save(WorkoutAccess workoutAccess);

    List<WorkoutAccess> findByWorkoutIdAndOwnerId(Long workoutId, Long ownerId);

    List<WorkoutAccess> findAllSharedWithUserId(Long userId);

    Optional<WorkoutAccess> findByIdAndOwnerId(Long id, Long ownerId);

    long deleteByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByWorkoutIdAndOwnerIdAndSharedWithUserId(Long workoutId, Long ownerId, Long sharedWithUserId);

    boolean existsByWorkoutIdAndSharedWithUserIdAndAccessLevel(Long workoutId, Long userId, AccessLevel accessLevel);
}
