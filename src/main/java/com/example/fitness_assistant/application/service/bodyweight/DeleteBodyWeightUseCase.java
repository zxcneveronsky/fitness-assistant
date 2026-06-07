package com.example.fitness_assistant.application.service.bodyweight;

import com.example.fitness_assistant.core.exception.BodyWeightNotFoundException;
import com.example.fitness_assistant.core.repository.BodyWeightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteBodyWeightUseCase {

    private final BodyWeightRepository bodyWeightRepository;

    @Transactional
    public void deleteBodyWeight(Long id, Long userId) {
        if (!bodyWeightRepository.existsById(id, userId)) {
            throw new BodyWeightNotFoundException(id);
        }
        bodyWeightRepository.deleteById(id, userId);
        log.info("Запись веса удалена | id={}", id);
    }
}
