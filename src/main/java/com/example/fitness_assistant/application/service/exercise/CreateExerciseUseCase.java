package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateExerciseUseCase {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Exercise createExercise(Exercise exercise) {
        exercise.setId(null);
        return exerciseRepository.save(exercise);
    }
}
