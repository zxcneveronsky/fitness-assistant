package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food,Long> {
    public Optional<Food> findByBarcode(String barcode);


}
