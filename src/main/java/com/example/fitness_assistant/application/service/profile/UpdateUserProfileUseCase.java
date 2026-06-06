package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.application.service.targets.TargetCalculationService;
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
public class UpdateUserProfileUseCase {

    private final UserProfileRepository userProfileRepository;
    private final TargetsRepository targetsRepository;
    private final TargetCalculationService targetCalculationService;

    @Transactional
    public UserProfile updateUserProfile(Long userId, UserProfile profileUpdate) {
        UserProfile updatedProfile = userProfileRepository.findById(userId)
                .map(existingProfile -> {
                    existingProfile.setName(profileUpdate.getName() != null ? profileUpdate.getName() : existingProfile.getName());
                    existingProfile.setBirthDate(profileUpdate.getBirthDate() != null ? profileUpdate.getBirthDate() : existingProfile.getBirthDate());
                    existingProfile.setWeight(profileUpdate.getWeight() != null ? profileUpdate.getWeight() : existingProfile.getWeight());
                    existingProfile.setHeight(profileUpdate.getHeight() != null ? profileUpdate.getHeight() : existingProfile.getHeight());
                    existingProfile.setGender(profileUpdate.getGender() != null ? profileUpdate.getGender() : existingProfile.getGender());
                    return userProfileRepository.save(existingProfile);
                })
                .orElseThrow(() -> new UserProfileNotFoundException());

        targetsRepository.findById(userId).ifPresent(targets -> {
            if (Boolean.TRUE.equals(targets.getUseAutopilot())) {
                targetCalculationService.applyAutoTargets(targets, updatedProfile);
                targetsRepository.save(targets);
            }
        });

        log.info("Профиль обновлён | userId={}", updatedProfile.getId());
        return updatedProfile;
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
        Targets savedTargets = targetsRepository.save(targets);

        log.info("Статус автопилота обновлён | userId={} | enabled={}", userId, enabled);
        return savedTargets;
    }
}
