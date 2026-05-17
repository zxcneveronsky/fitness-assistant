package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.TargetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTargetsRepository extends JpaRepository<TargetsEntity, Long> {}
