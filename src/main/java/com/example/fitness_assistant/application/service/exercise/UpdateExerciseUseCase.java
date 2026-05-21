package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.MuscleNotFoundException;
import com.example.fitness_assistant.core.model.Exercise;
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
        Long id = exerciseUpdate.getId();
        Exercise exercise = exerciseRepository.findById(id)
                .map(existingExercise -> {
                    existingExercise.setName(exerciseUpdate.getName() != null ? exerciseUpdate.getName() : existingExercise.getName());
                    existingExercise.setDescription(exerciseUpdate.getDescription() != null ? exerciseUpdate.getDescription() : existingExercise.getDescription());
                    if (exerciseUpdate.getMuscles() != null && !exerciseUpdate.getMuscles().isEmpty()) {
                        List<Long> ids = exerciseUpdate.getMuscles().stream().map(Muscle::getId).toList();
                        List<Muscle> muscles = muscleRepository.findAllById(ids);
                        if (muscles.size() != ids.size()) {
                            List<Long> foundIds = muscles.stream().map(Muscle::getId).toList();
                            Long missing = ids.stream().filter(muscleId -> !foundIds.contains(muscleId)).findFirst().orElse(ids.getFirst());
                            throw new MuscleNotFoundException(missing);
                        }
                        existingExercise.setMuscles(muscles);
                    }
                    return exerciseRepository.save(existingExercise);
                })
                .orElseThrow(() -> new ExerciseNotFoundException(id));
        log.info("Упражнение обновлено | id={}", id);
        return exercise;
    }
}

