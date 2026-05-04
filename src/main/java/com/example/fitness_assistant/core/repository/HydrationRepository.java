package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Hydration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface HydrationRepository {
    Optional<Hydration> findById(Long id, Long userId);

    Page<Hydration> searchHydration(LocalDateTime localDateTime, Long userId, Pageable pageable);

    Hydration save(Hydration hydration);

    void deleteById(Long id, Long userId);

    boolean existsById(Long id, Long userId);
}
