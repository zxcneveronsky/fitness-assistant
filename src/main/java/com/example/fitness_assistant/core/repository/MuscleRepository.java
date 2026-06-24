package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Muscle;

import java.util.List;

public interface MuscleRepository {
    boolean existsById(Long id);
    List<Muscle> findAllById(List<Long> ids);
    List<Muscle> searchMuscle(String name);
}
