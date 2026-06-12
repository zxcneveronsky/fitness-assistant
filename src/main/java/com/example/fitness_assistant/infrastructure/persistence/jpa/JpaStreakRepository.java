package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.StreakEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStreakRepository extends JpaRepository<StreakEntity, Long> {
}
