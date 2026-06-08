package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository {
    Optional<WorkoutSession> findById(Long id,Long userId);
    Page<WorkoutSession> findAllByUserId(Long userId, Pageable pageable);
    List<WorkoutSession> findAllByIdIn(List<Long> ids);
    WorkoutSession save(WorkoutSession workoutSession);
    void deleteById(Long id,Long userId);
    boolean existsById(Long id, Long userId);
}
