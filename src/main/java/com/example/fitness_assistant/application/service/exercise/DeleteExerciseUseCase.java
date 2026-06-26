package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteExerciseUseCase {

    private final ExerciseRepository exerciseRepository;
    @Transactional
    public void deleteExercise(Long exerciseId) {
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new ExerciseNotFoundException(exerciseId);
        }
        exerciseRepository.deleteById(exerciseId);
        log.info("Упражнение удалено | id={}", exerciseId);
    }
}
