package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @EntityGraph(attributePaths = "muscles")
    Optional<Exercise> findById(Long id);

    @EntityGraph(attributePaths = "muscles")
    Page<Exercise> findAll(Pageable pageable);

    @Query("""
    SELECT DISTINCT e FROM Exercise e 
    LEFT JOIN FETCH e.muscles m 
    WHERE LOWER(e.exerciseName) LIKE LOWER(CONCAT('%', :query, '%')) 
       OR LOWER(m.muscleGroup) LIKE LOWER(CONCAT('%', :query, '%')) 
       OR LOWER(m.muscleDetail) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<Exercise> searchExercises(@Param("query") String query, Pageable pageable);
}