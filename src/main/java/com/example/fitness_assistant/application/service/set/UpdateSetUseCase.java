package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.SetNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSetUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional
    public Set updateSet(Long userId, Set set) {
        Long setId = set.getId();
        Long sessionId = set.getSessionId();

        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }

        Set updatedSet = setRepository.findById(setId, sessionId).map(existingSet -> {
            existingSet.setReps(set.getReps() != null ? set.getReps() : existingSet.getReps());
            existingSet.setWeight(set.getWeight() != null ? set.getWeight() : existingSet.getWeight());
            return setRepository.save(existingSet);
        }).orElseThrow(()->new SetNotFoundException(setId));
        log.info("Подход обновлен | id={}", setId);
        return updatedSet;
    }

}
