package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
    List<Exercise> findByMuscleGroupOrMuscleDetail(String muscleGroup,String muscleDetail);
    List<Exercise> findByExerciseName(String exerciseName);

}
