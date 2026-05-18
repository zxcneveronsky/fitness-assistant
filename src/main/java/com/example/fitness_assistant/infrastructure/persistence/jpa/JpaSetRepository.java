package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.SetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSetRepository extends JpaRepository<SetEntity, Long> {
    @EntityGraph(attributePaths = {"session", "exercise"})
    Optional<SetEntity> findByIdAndSessionId(Long id, Long sessionId);
    void deleteByIdAndSessionId(Long id, Long sessionId);
    boolean existsByIdAndSessionId(Long id, Long sessionId);
    Page<SetEntity> findAllBySessionIdAndExerciseId(Long sessionId, Long exerciseId, Pageable pageable);
}
