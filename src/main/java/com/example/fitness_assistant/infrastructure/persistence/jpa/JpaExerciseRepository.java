package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.ExerciseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    @Query("SELECT e FROM ExerciseEntity e LEFT JOIN FETCH e.muscles WHERE e.id IN :ids ORDER BY e.name ASC")
    List<ExerciseEntity> findAllByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT COUNT(e) FROM ExerciseEntity e WHERE e.id IN :ids")
    long countAllByIdIn(@Param("ids") List<Long> ids);

    @Query(
            value = """
            SELECT DISTINCT e FROM ExerciseEntity e
            LEFT JOIN FETCH e.muscles m
            WHERE (cast(:name as text) IS NULL
            OR LOWER(e.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
            AND (:muscleId IS NULL OR m.id = :muscleId)
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e) FROM ExerciseEntity e
            LEFT JOIN e.muscles m
            WHERE (cast(:name as text) IS NULL
            OR LOWER(e.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
            AND (:muscleId IS NULL OR m.id = :muscleId)
            """)
    Page<ExerciseEntity> searchExercise(@Param("name") String name, @Param("muscleId") Long muscleId, Pageable pageable);

}
