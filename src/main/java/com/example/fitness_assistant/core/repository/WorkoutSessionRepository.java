package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import java.util.Optional;

public interface WorkoutSessionRepository {
    Optional<WorkoutSession> findById(Long id,Long userId);
    Page<WorkoutSession> findAllByUserId(Long userId, Pageable pageable);
    WorkoutSession save(WorkoutSession workoutSession);
    void deleteById(Long id,Long userId);
    boolean existsById(Long id, Long userId);

}
