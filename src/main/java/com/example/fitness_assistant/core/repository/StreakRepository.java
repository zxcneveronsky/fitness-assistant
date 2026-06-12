package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Streak;

import java.util.Optional;

public interface StreakRepository {
    Optional<Streak> findById(Long userId);
    Streak save(Streak streak);
}
