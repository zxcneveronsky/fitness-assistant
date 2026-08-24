package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.application.service.targets.TargetCalculationService;
import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.BodyWeightRepository;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserProfileUseCase {

    private final UserProfileRepository userProfileRepository;
    private final TargetsRepository targetsRepository;
    private final TargetCalculationService targetCalculationService;
    private final BodyWeightRepository bodyWeightRepository;

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

        if (profileUpdate.getWeight() != null) {
            bodyWeightRepository.save(new BodyWeight(null, userId, profileUpdate.getWeight(), LocalDate.now()));
        }

        log.info("Профиль обновлён | userId={}", updatedProfile.getUserId());
        return updatedProfile;
    }
}
