package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @EntityGraph(attributePaths = "muscles")
    Page<Exercise> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "muscles")
    Optional<Exercise> findByExerciseNameIgnoreCase(String exerciseName);

    @EntityGraph(attributePaths = "muscles")
    Page<Exercise> findByExerciseNameContainingIgnoreCase(String exerciseName, Pageable pageable);
}