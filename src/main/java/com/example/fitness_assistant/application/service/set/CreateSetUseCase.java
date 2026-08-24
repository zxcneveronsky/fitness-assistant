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

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateSetUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Set createSet(Long userId, Set set) {
        Long sessionId = set.getSessionId();
        Long exerciseId = set.getExerciseId();
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new ExerciseNotFoundException(exerciseId);
        }
        Set newSet = new Set(
                null,
                sessionId,
                exerciseId,
                set.getWeight(),
                set.getReps(),
                set.getCreatedAt()
        );
        Set savedSet = setRepository.save(newSet);
        log.info("Подход создан | id={} | sessionId={}", savedSet.getId(), savedSet.getSessionId());
        return savedSet;
    }
}
