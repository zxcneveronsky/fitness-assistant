package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FoodRepository {
    Optional<Food> findById(Long id);
    Page<Food> findAll(Pageable pageable);
    Page<Food> searchFood(String name, Pageable pageable);
    Food save(Food food);
    void deleteById(Long id);
    boolean existsById(Long id);
}
