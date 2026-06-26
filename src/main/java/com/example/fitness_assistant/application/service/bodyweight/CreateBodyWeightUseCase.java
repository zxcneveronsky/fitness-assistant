package com.example.fitness_assistant.application.service.bodyweight;

import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.core.repository.BodyWeightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateBodyWeightUseCase {

    private final BodyWeightRepository bodyWeightRepository;

    @Transactional
    public BodyWeight createBodyWeight(Long userId, BodyWeight bodyWeight) {
        BodyWeight toSave = new BodyWeight(
                null,
                userId,
                bodyWeight.getWeight(),
                bodyWeight.getMeasuredAt() != null ? bodyWeight.getMeasuredAt() : LocalDate.now()
        );
        BodyWeight savedBodyWeight = bodyWeightRepository.save(toSave);
        log.info("Запись веса создана | id={} | weight={}", savedBodyWeight.getId(), savedBodyWeight.getWeight());
        return savedBodyWeight;
    }
}
