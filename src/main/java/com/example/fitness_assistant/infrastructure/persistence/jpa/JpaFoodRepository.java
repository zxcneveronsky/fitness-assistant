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
        WHERE (cast(:name as text) IS NULL
        OR LOWER(f.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
        OR LOWER(f.brands) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
        """,
            countQuery = """
        SELECT COUNT(f) FROM FoodEntity f
        WHERE (cast(:name as text) IS NULL
        OR LOWER(f.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
        OR LOWER(f.brands) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
        """)
    Page<FoodEntity> searchFood(@Param("name") String name, Pageable pageable);}
