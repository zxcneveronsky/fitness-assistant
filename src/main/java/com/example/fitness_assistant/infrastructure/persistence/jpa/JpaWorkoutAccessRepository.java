package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.core.model.workoutaccess.AccessLevel;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaWorkoutAccessRepository extends JpaRepository<WorkoutAccessEntity, Long> {

    @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout WHERE wa.workout.id = :workoutId AND wa.owner.id = :ownerId")
    List<WorkoutAccessEntity> findByWorkoutIdAndOwnerId(@Param("workoutId") Long workoutId, @Param("ownerId") Long ownerId);

    @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout WHERE wa.sharedWithUser.id = :userId")
    List<WorkoutAccessEntity> findAllBySharedWithUserId(@Param("userId") Long userId);

    Optional<WorkoutAccessEntity> findByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByOwnerIdAndSharedWithUserIdAndWorkoutId(Long ownerId, Long sharedWithUserId, Long workoutId);

    boolean existsBySharedWithUserIdAndWorkoutIdAndAccessLevel(Long userId, Long workoutId, AccessLevel accessLevel);
}
