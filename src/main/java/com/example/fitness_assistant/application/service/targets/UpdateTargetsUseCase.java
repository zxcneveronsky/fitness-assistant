package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.update.UpdateTargetsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateTargetsUseCase {

    private final UserProfileRepository userProfileRepository;
    private final TargetCalculationService targetCalculationService;


    @Transactional
    public UserProfile updateTargets(UserDetails userDetails, UpdateTargetsRequest request) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));

        if (request.targetKcal() != null || targetCalculationService.hasMacros(request)) {
            profile.setUseAutopilot(false);
            targetCalculationService.applyManualTargets(profile, request);
        }
        return userProfileRepository.save(profile);
    }
}
