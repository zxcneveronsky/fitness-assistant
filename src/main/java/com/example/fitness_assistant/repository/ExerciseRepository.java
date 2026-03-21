package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByExerciseNameIgnoreCase(String exerciseName);
}