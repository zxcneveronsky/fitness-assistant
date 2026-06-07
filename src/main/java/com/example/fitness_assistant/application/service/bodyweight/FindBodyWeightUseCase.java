package com.example.fitness_assistant.application.service.bodyweight;

import com.example.fitness_assistant.core.exception.BodyWeightNotFoundException;
import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.core.repository.BodyWeightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindBodyWeightUseCase {

    private final BodyWeightRepository bodyWeightRepository;

    @Transactional(readOnly = true)
    public BodyWeight findById(Long id, Long userId) {
        return bodyWeightRepository.findById(id, userId)
                .orElseThrow(() -> new BodyWeightNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<BodyWeight> findByDateRange(Long userId, LocalDate from, LocalDate to) {
        List<BodyWeight> bodyWeights = bodyWeightRepository.findByUserIdAndDateBetween(userId, from, to);
        log.info("Поиск записей веса | userId={} | from={} | to={} | найдено={}", userId, from, to, bodyWeights.size());
        return bodyWeights;
    }

    @Transactional(readOnly = true)
    public Optional<BodyWeight> findLatest(Long userId) {
        return bodyWeightRepository.findLatestByUserId(userId);
    }
}
