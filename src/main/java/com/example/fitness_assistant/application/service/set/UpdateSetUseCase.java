package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.SetNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.SetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSetUseCase {
    private final SetRepository setRepository;

    @Transactional
    public Set updateSet(Long userId, Set setUpdate) {
        Long setId = setUpdate.getId();
        Long sessionId = setUpdate.getSessionId();

        Set updatedSet = setRepository.findById(setId, sessionId, userId).map(existingSet -> {
            existingSet.setReps(setUpdate.getReps() != null ? setUpdate.getReps() : existingSet.getReps());
            existingSet.setWeight(setUpdate.getWeight() != null ? setUpdate.getWeight() : existingSet.getWeight());
            return setRepository.save(existingSet);
        }).orElseThrow(() -> new SetNotFoundException(setId));
        log.info("Подход обновлён | id={}", setId);
        return updatedSet;
    }
}
