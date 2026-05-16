package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateTargetsUseCase {

    private final UserProfileRepository userProfileRepository;
    private final TargetCalculationService targetCalculationService;


    @Transactional
    public UserProfile updateTargets(UserDetails userDetails, Targets request) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));

        if (request.getTargetKcal() != null || targetCalculationService.hasMacros(request) || request.getTargetHydration() != null) {
            profile.setUseAutopilot(false); // Ставим false , чтобы потом при обновлении профиля(веса) не пересчитывались targets
            targetCalculationService.applyManualTargets(profile, request);
        }
        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Цели обновлены | userId={}", savedProfile.getId());
        return savedProfile;
    }
}
