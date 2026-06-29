package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.exception.TargetsNotFoundException;
import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public Targets updateTargets(Long userId, Targets targetsUpdate) {
        Targets targets = targetsRepository.findById(userId)
                .orElseThrow(() -> new TargetsNotFoundException());

        if (targetsUpdate.getTargetKcal() != null || targetCalculationService.hasMacros(targetsUpdate) || targetsUpdate.getTargetHydration() != null) {
            targets.setUseAutopilot(false);
            targetCalculationService.applyManualTargets(targets, targetsUpdate);
        }
        Targets updatedTargets = targetsRepository.save(targets);
        log.info("Цели обновлены | userId={}", updatedTargets.getProfileId());
        return updatedTargets;
    }

    @Transactional
    public Targets updateAutopilotStatus(Long userId, boolean enabled) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException());

        Targets targets = targetsRepository.findById(userId)
                .orElseThrow(() -> new TargetsNotFoundException());
        targets.setUseAutopilot(enabled);
        if (enabled) {
            targetCalculationService.applyAutoTargets(targets, profile);
        }
        Targets updatedTargets = targetsRepository.save(targets);

        log.info("Статус автопилота обновлён | userId={} | enabled={}", userId, enabled);
        return updatedTargets;
    }
}
