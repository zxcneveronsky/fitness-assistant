package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.ExerciseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    @EntityGraph(attributePaths = {"muscles"})
    @Override
    Page<ExerciseEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"muscles"})
    @Query("SELECT e FROM ExerciseEntity e WHERE e.id IN :ids")
    List<ExerciseEntity> findAllByIdIn(@Param("ids") List<Long> ids);

    @Query(
            value = """
            SELECT DISTINCT e FROM ExerciseEntity e
            LEFT JOIN FETCH e.muscles m
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%'))
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e) FROM ExerciseEntity e
            LEFT JOIN e.muscles m
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    Page<ExerciseEntity> searchExercise(@Param("query") String query, Pageable pageable);

}