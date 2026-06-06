package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.exception.TargetsNotFoundException;
import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindTargetsUseCase {

    private final TargetsRepository targetsRepository;

    @Transactional(readOnly = true)
    public Targets findById(Long userId) {
        Targets targets = targetsRepository.findById(userId)
                .orElseThrow(() -> new TargetsNotFoundException());
        log.info("Цели найдены | userId={}", userId);
        return targets;
    }
}
