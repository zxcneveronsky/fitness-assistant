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
    Optional<WorkoutEntity> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"exerciseIds"})
    Page<WorkoutEntity> findAllByUserId(Long userId, Pageable pageable);

    @Query(
            value = """
            SELECT DISTINCT w FROM WorkoutEntity w
            LEFT JOIN FETCH w.exerciseIds
            WHERE w.user.id = :userId
            AND LOWER(w.name) LIKE LOWER(CONCAT('%', :query, '%'))
            """,
            countQuery = """
            SELECT COUNT(w) FROM WorkoutEntity w
            WHERE w.user.id = :userId
            AND LOWER(w.name) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    Page<WorkoutEntity> searchWorkout(@Param("query") String query, Long userId, Pageable pageable);
}
