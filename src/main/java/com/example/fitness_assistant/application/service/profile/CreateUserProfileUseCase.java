package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.application.service.targets.TargetCalculationService;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.UserProfile;
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
    private final TargetCalculationService targetCalculationService;

    @Transactional
    public UserProfile createUserProfile(UserDetails userDetails, UserProfile userProfile) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userProfile.setId(userId);
        userProfile.setUseAutopilot(true); // Ставим всегда true , чтобы сразу просчитывались targets при изменениях профиля(веса)
        targetCalculationService.applyAutoTargets(userProfile);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        log.info("Профиль создан | userId={}", savedProfile.getId());
        return savedProfile;
    }
}
