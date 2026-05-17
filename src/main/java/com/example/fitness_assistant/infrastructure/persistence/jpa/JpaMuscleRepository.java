package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.MuscleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaMuscleRepository extends JpaRepository<MuscleEntity,Long> {}
