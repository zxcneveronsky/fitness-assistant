package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaWorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<WorkoutEntity> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);

    @Query("SELECT w FROM WorkoutEntity w WHERE w.user.id = :userId ORDER BY w.id DESC")
    Page<WorkoutEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = """
            SELECT w FROM WorkoutEntity w
            WHERE w.user.id = :userId
            AND (cast(:query as text) IS NULL
                 OR LOWER(w.name) LIKE LOWER(CONCAT('%', cast(:query as text), '%')))
            ORDER BY w.id DESC
            """,
            countQuery = """
            SELECT COUNT(w) FROM WorkoutEntity w
            WHERE w.user.id = :userId
            AND (cast(:query as text) IS NULL
                 OR LOWER(w.name) LIKE LOWER(CONCAT('%', cast(:query as text), '%')))
            """)
    Page<WorkoutEntity> searchWorkout(@Param("query") String query, @Param("userId") Long userId, Pageable pageable);
}
