package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.model.exercise.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class FindExerciseUseCase {

    private final ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public Page<Exercise> searchExercise(String name, Long muscleId, Pageable pageable) {
        Page<Exercise> exercises = exerciseRepository.searchExercise(name, muscleId, pageable);
        log.info("Поиск упражнений завершён | name='{}' | найдено={} | страница={}/{}",
                name,
                exercises.getTotalElements(),
                exercises.getNumber() + 1,
                exercises.getTotalPages());
        return exercises;
    }

    @Transactional(readOnly = true)
    public Exercise findById(Long exerciseId){
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        log.info("Упражнение найдено | id={}", exerciseId);
        return exercise;

    }
}
