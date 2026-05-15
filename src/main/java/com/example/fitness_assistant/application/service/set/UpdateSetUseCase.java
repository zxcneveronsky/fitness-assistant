package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.SetNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSetUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    public Set updateSet(UserDetails userDetails, Set set) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Long id = set.getId();
        Long sessionId = set.getSessionId();
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        if (!setRepository.existsById(id, sessionId)) {
            throw new SetNotFoundException(id);
        }

        Set updatedSet = setRepository.findById(id, sessionId).map(existingSet -> {
            existingSet.setReps(set.getReps() != null ? set.getReps() : existingSet.getReps());
            existingSet.setWeight(set.getWeight() != null ? set.getWeight() : existingSet.getWeight());
            return setRepository.save(existingSet);
        }).orElseThrow(()->new SetNotFoundException(id));
        log.info("Подход обновлен | id={}", id);
        return updatedSet;
    }

}
