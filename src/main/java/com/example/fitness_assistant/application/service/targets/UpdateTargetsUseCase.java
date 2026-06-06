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
public class UpdateTargetsUseCase {

    private final TargetsRepository targetsRepository;
    private final TargetCalculationService targetCalculationService;

    @Transactional
    public Targets updateTargets(Long userId, Targets request) {
        Targets targets = targetsRepository.findById(userId)
                .orElseThrow(() -> new TargetsNotFoundException());

        if (request.getTargetKcal() != null || targetCalculationService.hasMacros(request) || request.getTargetHydration() != null) {
            targets.setUseAutopilot(false);
            targetCalculationService.applyManualTargets(targets, request);
        }
        Targets savedTargets = targetsRepository.save(targets);
        log.info("Цели обновлены | userId={}", savedTargets.getProfileId());
        return savedTargets;
    }
}
