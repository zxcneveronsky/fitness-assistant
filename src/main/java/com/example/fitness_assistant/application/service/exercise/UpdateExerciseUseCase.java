package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.MuscleNotFoundException;
import com.example.fitness_assistant.core.model.exercise.Exercise;
import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateExerciseUseCase {

    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository muscleRepository;

    @Transactional
    public Exercise updateExercise(Exercise exerciseUpdate) {
        Long exerciseId = exerciseUpdate.getId();
        Exercise updatedExercise = exerciseRepository.findById(exerciseId)
                .map(existingExercise -> {
                    existingExercise.setName(exerciseUpdate.getName() != null ? exerciseUpdate.getName() : existingExercise.getName());
                    existingExercise.setDescription(exerciseUpdate.getDescription() != null ? exerciseUpdate.getDescription() : existingExercise.getDescription());
                    if (exerciseUpdate.getMuscles() != null && !exerciseUpdate.getMuscles().isEmpty()) {
                        List<Long> muscleIds = exerciseUpdate.getMuscles().stream().map(Muscle::getId).toList();
                        List<Muscle> muscles = muscleRepository.findAllById(muscleIds);
                        if (muscles.size() != muscleIds.size()) {
                            List<Long> foundMuscleIds = muscles.stream().map(Muscle::getId).toList();
                            Long missingMuscleId = muscleIds.stream().filter(muscleId -> !foundMuscleIds.contains(muscleId)).findFirst().orElse(muscleIds.getFirst());
                            throw new MuscleNotFoundException(missingMuscleId);
                        }
                        existingExercise.setMuscles(muscles);
                    }
                    return exerciseRepository.save(existingExercise);
                })
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        log.info("Упражнение обновлено | id={}", updatedExercise.getId());
        return updatedExercise;
    }
}

