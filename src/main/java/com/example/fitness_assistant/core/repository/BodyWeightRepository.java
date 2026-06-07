package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.BodyWeight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BodyWeightRepository {

    Optional<BodyWeight> findById(Long id, Long userId);

    List<BodyWeight> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);

    Optional<BodyWeight> findLatestByUserId(Long userId);

    BodyWeight save(BodyWeight bodyWeight);

    void deleteById(Long id, Long userId);

    boolean existsById(Long id, Long userId);
}
