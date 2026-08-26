package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutAccessEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaWorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<WorkoutEntity> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);

    @Query("SELECT w FROM WorkoutEntity w WHERE w.user.id = :userId ORDER BY w.id DESC")
    Page<WorkoutEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
            SELECT w FROM WorkoutEntity w
            WHERE w.user.id = :userId
            AND LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY w.id DESC
            """)
    Page<WorkoutEntity> searchByName(@Param("name") String name, @Param("userId") Long userId, Pageable pageable);

    @Query("""
            SELECT COUNT(w) > 0 FROM WorkoutEntity w
            WHERE w.id = :workoutId
            AND (w.user.id = :userId
                 OR EXISTS (SELECT 1 FROM WorkoutAccessEntity wa
                            WHERE wa.workout.id = w.id AND wa.sharedWithUser.id = :userId))
            """)
    boolean existsAccessible(@Param("workoutId") Long workoutId, @Param("userId") Long userId);

    @Query("""
            SELECT w FROM WorkoutEntity w
            WHERE w.id IN :ids
            AND (w.user.id = :userId
                 OR EXISTS (SELECT 1 FROM WorkoutAccessEntity wa
                            WHERE wa.workout.id = w.id AND wa.sharedWithUser.id = :userId))
            """)
    List<WorkoutEntity> findAllAccessibleByIdIn(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("""
            SELECT w FROM WorkoutEntity w
            WHERE w.id = :id
            AND (w.user.id = :userId
                 OR EXISTS (SELECT 1 FROM WorkoutAccessEntity wa
                            WHERE wa.workout.id = w.id AND wa.sharedWithUser.id = :userId))
            """)
    Optional<WorkoutEntity> findAccessibleById(@Param("id") Long id, @Param("userId") Long userId);

    @Query("""
            SELECT w FROM WorkoutEntity w
            WHERE w.id = :id
            AND (w.user.id = :userId
                 OR EXISTS (SELECT 1 FROM WorkoutAccessEntity wa
                            WHERE wa.workout.id = w.id AND wa.sharedWithUser.id = :userId
                            AND wa.accessLevel = :accessLevel))
            """)
    Optional<WorkoutEntity> findAccessibleByIdWithLevel(@Param("id") Long id,
                                                        @Param("userId") Long userId,
                                                        @Param("accessLevel") WorkoutAccessEntity.AccessLevel accessLevel);
}
