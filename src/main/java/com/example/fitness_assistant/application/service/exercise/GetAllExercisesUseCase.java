package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllExercisesUseCase {

    private final ExerciseRepository exerciseRepository;

    public Page<Exercise> getAllExercises(Pageable pageable) {
        Page<Exercise> exercises = exerciseRepository.findAll(pageable);
        log.info("Поиск упражнений завершён | найдено={} | страница={}/{}",
                exercises.getTotalElements(),
                exercises.getNumber() + 1,
                exercises.getTotalPages());
        return exercises;
    }
}
