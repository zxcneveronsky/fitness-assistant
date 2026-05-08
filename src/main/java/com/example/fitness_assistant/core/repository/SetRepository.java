package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SetRepository {
    Optional<Set> findById(Long id, Long sessionId);
    Page<Set> findAllBySessionIdAndExerciseId(Long sessionId, Long exerciseId, Pageable pageable);
    Set save(Set set);
    void deleteById(Long id,Long sessionId);
    boolean existsById(Long id,Long sessionId);
}
