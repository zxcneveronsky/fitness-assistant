package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseMuscleRepository extends JpaRepository<ExerciseMuscle, Long> {

    @Query("SELECT DISTINCT e FROM Exercise e " +
            "JOIN FETCH e.muscles m " +
            "WHERE LOWER(m.muscleGroup) LIKE LOWER(CONCAT('%', :muscle, '%')) " +
            "OR LOWER(m.muscleDetail) LIKE LOWER(CONCAT('%', :muscle, '%'))")
    Page<Exercise> findDistinctExercisesByMuscle(@Param("muscle") String muscle, Pageable pageable);

    void deleteByExercise(Exercise exercise);
}