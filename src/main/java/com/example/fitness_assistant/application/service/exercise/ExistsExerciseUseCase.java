package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExistsExerciseUseCase {

    private final ExerciseRepository exerciseRepository;

    public boolean existsExercise(Long id) {
        return exerciseRepository.existsById(id);
    }
}
