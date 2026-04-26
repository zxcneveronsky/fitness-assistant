package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFoodRepository extends JpaRepository<FoodEntity, Long> {
    Page<FoodEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}