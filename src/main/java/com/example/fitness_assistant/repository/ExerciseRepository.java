package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByExerciseNameIgnoreCase(String exerciseName);
    Page<Exercise> findByExerciseNameContainingIgnoreCase(String exerciseName, Pageable pageable);
}