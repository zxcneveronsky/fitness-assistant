package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.MuscleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaMuscleRepository extends JpaRepository<MuscleEntity, Long> {

    List<MuscleEntity> findAllByOrderByNameAsc();

    @Query("""
            SELECT m FROM MuscleEntity m
            WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\'
            ORDER BY m.name ASC
            """)
    List<MuscleEntity> searchByName(@Param("name") String name);
}
