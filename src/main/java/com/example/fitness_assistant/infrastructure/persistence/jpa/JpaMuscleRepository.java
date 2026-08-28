package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.MuscleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface JpaMuscleRepository extends JpaRepository<MuscleEntity, Long> {

    List<MuscleEntity> findAllByOrderByNameAsc();

    List<MuscleEntity> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}
