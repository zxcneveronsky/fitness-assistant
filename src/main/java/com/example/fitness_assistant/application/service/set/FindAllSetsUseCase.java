package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindAllSetsUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public Page<Set> findAll(Long sessionId, Long exerciseId, Long userId, Pageable pageable) {
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        if (!exerciseRepository.existsById(exerciseId)){
            throw new ExerciseNotFoundException(exerciseId);
        }
        Page<Set> sets = setRepository.findAllBySessionIdAndExerciseId(sessionId, exerciseId, pageable);
        log.info("Поиск подходов завершён | найдено={} | страница={}/{}",
                sets.getTotalElements(),
                sets.getNumber() + 1,
                sets.getTotalPages());
        return sets;
    }

}
