package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Muscle;

import java.util.List;
import java.util.Optional;

public interface MuscleRepository {
    boolean existsById(Long id);
    Optional<Muscle> findById(Long id);
    List<Muscle> findAllById(List<Long> ids);
    List<Muscle> searchMuscle(String name);
    Muscle save(Muscle muscle);
    void deleteById(Long id);
}
