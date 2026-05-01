package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateExerciseUseCase {

    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository muscleRepository;

    @Transactional
    public Exercise createExercise(Exercise exercise) {
        exercise.setId(null);
        exercise.setMuscles(exercise.getMuscles().stream()
                .map(muscle -> muscleRepository.getReferenceById(muscle.getId()))
                .toList()
        );
        return exerciseRepository.save(exercise);
    }

}
