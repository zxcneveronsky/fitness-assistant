package com.example.fitness_assistant.application.service.bodyweight;

import com.example.fitness_assistant.core.exception.BodyWeightNotFoundException;
import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.core.repository.BodyWeightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateBodyWeightUseCase {

    private final BodyWeightRepository bodyWeightRepository;

    @Transactional
    public BodyWeight updateBodyWeight(Long userId, BodyWeight bodyWeightUpdate) {
        Long id = bodyWeightUpdate.getId();
        BodyWeight updatedBodyWeight = bodyWeightRepository.findById(id, userId)
                .map(existingBodyWeight -> {
                    existingBodyWeight.setWeightKg(bodyWeightUpdate.getWeightKg() != null ? bodyWeightUpdate.getWeightKg() : existingBodyWeight.getWeightKg());
                    existingBodyWeight.setMeasuredAt(bodyWeightUpdate.getMeasuredAt() != null ? bodyWeightUpdate.getMeasuredAt() : existingBodyWeight.getMeasuredAt());
                    return bodyWeightRepository.save(existingBodyWeight);
                })
                .orElseThrow(() -> new BodyWeightNotFoundException(id));
        log.info("Запись веса обновлена | id={}", id);
        return updatedBodyWeight;
    }
}
