package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllExercisesUseCase {

    private final ExerciseRepository exerciseRepository;

    public Page<Exercise> getAllExercises(Pageable pageable) {
        return exerciseRepository.findAll(pageable);
    }
}
