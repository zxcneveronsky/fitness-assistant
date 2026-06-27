package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.model.sessiondetail.SetDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SetRepository {
    Optional<Set> findById(Long id, Long sessionId);
    Page<Set> findBySessionIdAndExerciseId(Long sessionId, Long exerciseId, Pageable pageable);
    Page<Set> findByExerciseIdAndUserIdAndStartTimeBetween(Long exerciseId, Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<SetDetail> findAllSetDetailBySessionId(Long sessionId);
    Set save(Set set);
    void deleteById(Long id, Long sessionId);
    boolean existsById(Long id, Long sessionId);
}
