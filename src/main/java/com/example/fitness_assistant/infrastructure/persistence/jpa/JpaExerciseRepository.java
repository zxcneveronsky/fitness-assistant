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

    @Query(value = "SELECT e.id FROM ExerciseEntity e GROUP BY e.id, e.name ORDER BY e.name ASC",
            countQuery = "SELECT COUNT(e) FROM ExerciseEntity e")
    Page<Long> findPageIds(Pageable pageable);

    @Query(value = """
            SELECT e.id FROM ExerciseEntity e
            LEFT JOIN e.muscles m
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))
            GROUP BY e.id, e.name
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e.id) FROM ExerciseEntity e
            LEFT JOIN e.muscles m
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<Long> findPageIdsByName(@Param("name") String name, Pageable pageable);

    @Query(value = """
            SELECT e.id FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE m.id = :muscleId
            GROUP BY e.id, e.name
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e.id) FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE m.id = :muscleId
            """)
    Page<Long> findPageIdsByMuscleId(@Param("muscleId") Long muscleId, Pageable pageable);

    @Query(value = """
            SELECT e.id FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE m.id = :muscleId
            AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            GROUP BY e.id, e.name
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e.id) FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE m.id = :muscleId
            AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<Long> findPageIdsByMuscleIdAndName(@Param("muscleId") Long muscleId, @Param("name") String name, Pageable pageable);
}
