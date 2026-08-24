package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.SetNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindSetUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public Page<Set> findBySessionIdAndExerciseId(Long userId, Long sessionId, Long exerciseId, Pageable pageable) {
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new ExerciseNotFoundException(exerciseId);
        }
        Page<Set> sets = setRepository.findBySessionIdAndExerciseId(sessionId, exerciseId, pageable);
        log.info("Поиск подходов завершён | userId={} | sessionId={} | exerciseId={} | найдено={} | страница={}/{}",
                userId, sessionId, exerciseId,
                sets.getTotalElements(),
                sets.getNumber() + 1,
                sets.getTotalPages());
        return sets;
    }

    @Transactional(readOnly = true)
    public Set findById(Long userId, Long sessionId, Long setId) {
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        Set set = setRepository.findById(setId, sessionId)
                .orElseThrow(() -> new SetNotFoundException(setId));
        log.info("Подход найден | userId={} | sessionId={} | id={}", userId, sessionId, setId);
        return set;
    }
}
