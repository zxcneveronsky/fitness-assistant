package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.fitness_assistant.entity.Exercise;

public interface ExerciseMuscleRepository extends JpaRepository<ExerciseMuscle, Long> {
    @Query("SELECT DISTINCT em.exercise FROM ExerciseMuscle em " +
            "WHERE LOWER(em.muscleGroup) = LOWER(:muscle) OR LOWER(em.muscleDetail) = LOWER(:muscle)")
    Page<Exercise> findDistinctExercisesByMuscle(@Param("muscle") String muscle, Pageable pageable);    void deleteByExercise(Exercise exercise);
}