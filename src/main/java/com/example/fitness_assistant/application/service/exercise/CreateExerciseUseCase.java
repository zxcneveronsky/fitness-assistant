package com.example.fitness_assistant.application.service.exercise;

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
public class CreateExerciseUseCase {

    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository muscleRepository;

    @Transactional
    public Exercise createExercise(Exercise exercise) {
        exercise.setId(null);
        List<Long> ids = exercise.getMuscles().stream().map(Muscle::getId).toList();
        List<Muscle> muscles = muscleRepository.findAllById(ids);
        if (muscles.size() != ids.size()) {
            List<Long> foundIds = muscles.stream().map(Muscle::getId).toList();
            Long missing = ids.stream().filter(id -> !foundIds.contains(id)).findFirst().orElse(ids.getFirst());
            throw new MuscleNotFoundException(missing);
        }
        exercise.setMuscles(muscles);
        Exercise savedExercise = exerciseRepository.save(exercise);
        log.info("Упражнение создано | id={} | название='{}'",
                savedExercise.getId(), savedExercise.getName());
        return savedExercise;
    }

}