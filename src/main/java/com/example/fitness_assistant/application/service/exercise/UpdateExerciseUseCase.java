package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateExerciseUseCase {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Exercise updateExercise(Exercise exerciseUpdate) {
        Long id = exerciseUpdate.getId();
        return exerciseRepository.findById(id)
                .map(existingExercise -> {
                    existingExercise.setName(exerciseUpdate.getName() != null ? exerciseUpdate.getName() : existingExercise.getName());
                    existingExercise.setDescription(exerciseUpdate.getDescription() != null ? exerciseUpdate.getDescription() : existingExercise.getDescription());
                    if (exerciseUpdate.getMuscles() != null && !exerciseUpdate.getMuscles().isEmpty()) {
                        existingExercise.setMuscles(exerciseUpdate.getMuscles());
                    }
                    return exerciseRepository.save(existingExercise);
                })
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }
}
