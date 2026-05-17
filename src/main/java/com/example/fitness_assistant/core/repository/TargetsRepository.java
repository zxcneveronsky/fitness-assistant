package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Targets;

import java.util.Optional;

public interface TargetsRepository {
    Optional<Targets> findById(Long profileId);
    Targets save(Targets targets);
    void deleteById(Long profileId);
    boolean existsById(Long profileId);
}
