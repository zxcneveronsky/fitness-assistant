package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.SetNotFoundException;
import com.example.fitness_assistant.core.repository.SetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteSetUseCase {
    private final SetRepository setRepository;

    @Transactional
    public void deleteSet(Long userId, Long sessionId, Long setId) {
        long deleted = setRepository.deleteById(setId, sessionId, userId);
        if (deleted == 0) {
            throw new SetNotFoundException(setId);
        }
        log.info("Подход удалён | id={}", setId);
    }
}
