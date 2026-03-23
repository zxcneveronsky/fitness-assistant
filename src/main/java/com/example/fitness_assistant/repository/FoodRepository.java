package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);
        Page<Food> findByNameContainingIgnoreCaseOrBrandsContainingIgnoreCase(String name, String brands, Pageable pageable);}
