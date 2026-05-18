package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.SetNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteSetUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional
    public void deleteSet(Long id, Long sessionId, Long userId) {
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        if (!setRepository.existsById(id, sessionId)) {
            throw new SetNotFoundException(id);
        }
        setRepository.deleteById(id, sessionId);
        log.info("Подход удален | id={}", id);
    }

}
