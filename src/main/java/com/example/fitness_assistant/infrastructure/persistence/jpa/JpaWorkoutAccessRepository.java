package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaWorkoutAccessRepository extends JpaRepository<WorkoutAccessEntity, Long> {

    @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout JOIN FETCH wa.owner JOIN FETCH wa.sharedWithUser WHERE wa.owner.id = :ownerId AND wa.workout.id = :workoutId")
    List<WorkoutAccessEntity> findByWorkoutIdAndOwnerId(@Param("workoutId") Long workoutId, @Param("ownerId") Long ownerId);

    @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout JOIN FETCH wa.owner JOIN FETCH wa.sharedWithUser WHERE wa.sharedWithUser.id = :userId")
    List<WorkoutAccessEntity> findAllBySharedWithUserId(@Param("userId") Long userId);

    Optional<WorkoutAccessEntity> findByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByWorkoutIdAndOwnerIdAndSharedWithUserId(Long workoutId, Long ownerId, Long sharedWithUserId);

    boolean existsByWorkoutIdAndSharedWithUserIdAndAccessLevel(Long workoutId, Long userId, AccessLevel accessLevel);

    boolean existsByWorkoutIdAndSharedWithUserId(Long workoutId, Long userId);
}
