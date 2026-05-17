package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.model.Exercise;
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

    public Page<Exercise> findExercise(String name, Long muscleId, Pageable pageable) {
        Page<Exercise> exercises = exerciseRepository.searchExercise(name, muscleId, pageable);
        log.info("Поиск упражнений завершён | name='{}' | найдено={} | страница={}/{}",
                name,
                exercises.getTotalElements(),
                exercises.getNumber() + 1,
                exercises.getTotalPages());
        return exercises;
    }

    @Transactional
    public Exercise findById(Long id){
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(()->new ExerciseNotFoundException(id));
        log.info("Упражнение найдено | id={}", id);
        return exercise;

    }
}
