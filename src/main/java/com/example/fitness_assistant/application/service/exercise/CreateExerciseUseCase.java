package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.MuscleNotFoundException;
import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateExerciseUseCase {

    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository muscleRepository;

    @Transactional
    public Exercise createExercise(Exercise exercise) {
        exercise.setId(null);
        exercise.setMuscles(exercise.getMuscles().stream()
                .map(muscle -> {
                    if (!muscleRepository.existsById(muscle.getId())) {
                        throw new MuscleNotFoundException(muscle.getId());
                    }
                    return muscleRepository.getReferenceById(muscle.getId());
                })
                .toList()
        );
        Exercise savedExercise = exerciseRepository.save(exercise);
        log.info("РЈРїСЂР°Р¶РЅРµРЅРёРµ СЃРѕР·РґР°РЅРѕ | id={} | РЅР°Р·РІР°РЅРёРµ='{}'",
                savedExercise.getId(), savedExercise.getName());
        return savedExercise;
    }

}

