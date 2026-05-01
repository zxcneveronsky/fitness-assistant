package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaFoodRepository extends JpaRepository<FoodEntity, Long> {
    @Query(value = """
        SELECT f FROM FoodEntity f
        WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(f.brands) LIKE LOWER(CONCAT('%', :query, '%'))
        """,
            countQuery = """
        SELECT COUNT(f) FROM FoodEntity f
        WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(f.brands) LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    Page<FoodEntity> searchFood(@Param("query") String query, Pageable pageable);}