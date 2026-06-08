package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutSessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface JpaWorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<WorkoutSessionEntity> findByIdAndUserId(Long id, Long userId);
    @Query("SELECT w FROM WorkoutSessionEntity w WHERE w.user.id = :userId ORDER BY w.startTime DESC")
    @EntityGraph(attributePaths = "user")
    Page<WorkoutSessionEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
    void deleteByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
