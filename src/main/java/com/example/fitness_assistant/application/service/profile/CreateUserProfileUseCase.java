package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.application.service.targets.TargetCalculationService;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUserProfileUseCase {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final TargetsRepository targetsRepository;
    private final TargetCalculationService targetCalculationService;

    @Transactional
    public UserProfile createUserProfile(UserDetails userDetails, UserProfile userProfile) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userProfile.setId(userId);
        UserProfile savedProfile = userProfileRepository.save(userProfile);

        Targets targets = new Targets();
        targets.setProfileId(userId);
        targets.setUseAutopilot(true);
        targetCalculationService.applyAutoTargets(targets, savedProfile);
        targetsRepository.save(targets);

        log.info("Профиль создан | userId={}", savedProfile.getId());
        return savedProfile;
    }
}
