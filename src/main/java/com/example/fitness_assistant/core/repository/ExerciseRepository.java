package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository {
    Optional<Exercise> findById(Long id);
    List<Exercise> findAllByIdIn(List<Long> ids);
    Page<Exercise> searchExercise(String name, Long muscleId, Pageable pageable);
    boolean existsAllByIdIn(List<Long> ids);
    Exercise save(Exercise exercise);
    void deleteById(Long id);
    boolean existsById(Long id);
}
