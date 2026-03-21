package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseMuscleRepository extends JpaRepository<ExerciseMuscle, Long> {
    List<ExerciseMuscle> findByMuscleGroupIgnoreCaseOrMuscleDetailIgnoreCase(String muscleGroup, String muscleDetail);
    List<ExerciseMuscle> findByExercise_ExerciseNameIgnoreCase(String exerciseName);
    void deleteByExercise(Exercise exercise);
}