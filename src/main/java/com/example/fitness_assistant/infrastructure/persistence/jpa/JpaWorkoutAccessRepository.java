package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaWorkoutAccessRepository extends JpaRepository<WorkoutAccessEntity, Long> {

    @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout JOIN FETCH wa.owner JOIN FETCH wa.sharedWithUser WHERE wa.owner.id = :ownerId AND wa.workout.id = :workoutId")
    List<WorkoutAccessEntity> findByWorkoutIdAndOwnerId(@Param("workoutId") Long workoutId, @Param("ownerId") Long ownerId);

    @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout JOIN FETCH wa.owner JOIN FETCH wa.sharedWithUser WHERE wa.sharedWithUser.id = :userId")
    List<WorkoutAccessEntity> findAllBySharedWithUserId(@Param("userId") Long userId);

    @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout JOIN FETCH wa.owner JOIN FETCH wa.sharedWithUser WHERE wa.id = :id AND wa.owner.id = :ownerId")
    Optional<WorkoutAccessEntity> findByIdAndOwnerId(@Param("id") Long id, @Param("ownerId") Long ownerId);

    @Modifying
    @Query("DELETE FROM WorkoutAccessEntity wa WHERE wa.id = :id AND wa.owner.id = :ownerId")
    long deleteByIdAndOwnerId(@Param("id") Long id, @Param("ownerId") Long ownerId);

    boolean existsByWorkoutIdAndOwnerIdAndSharedWithUserId(Long workoutId, Long ownerId, Long sharedWithUserId);

    boolean existsByWorkoutIdAndSharedWithUserIdAndAccessLevel(Long workoutId, Long userId, WorkoutAccessEntity.AccessLevel accessLevel);
}
