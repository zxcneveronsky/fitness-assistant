package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.hydration.DailyHydration;
import com.example.fitness_assistant.core.model.hydration.Hydration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface HydrationRepository {
    Optional<Hydration> findById(Long id, Long userId);
    Page<Hydration> searchHydration(Long userId, LocalDateTime consumedAt, Pageable pageable);
    Hydration save(Hydration hydration);
    void deleteById(Long id, Long userId);
    boolean existsById(Long id, Long userId);
    DailyHydration getDailyHydration(Long userId, LocalDateTime consumedAt);
}
