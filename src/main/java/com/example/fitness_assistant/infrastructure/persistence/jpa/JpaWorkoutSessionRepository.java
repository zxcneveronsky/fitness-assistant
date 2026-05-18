package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutSessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaWorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<WorkoutSessionEntity> findByIdAndUserId(Long id, Long userId);
    @EntityGraph(attributePaths = "user")
    Page<WorkoutSessionEntity> findAllByUserId(Long userId, Pageable pageable);
    void deleteByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
