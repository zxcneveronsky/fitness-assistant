package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.application.service.targets.TargetCalculationService;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.BodyWeightRepository;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUserProfileUseCase {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final TargetsRepository targetsRepository;
    private final TargetCalculationService targetCalculationService;
    private final BodyWeightRepository bodyWeightRepository;

    @Transactional
    public UserProfile createUserProfile(Long userId, UserProfile userProfile) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        UserProfile newProfile = new UserProfile(
                userId,
                userProfile.getName(),
                userProfile.getBirthDate(),
                userProfile.getWeight(),
                userProfile.getHeight(),
                userProfile.getGender()
        );
        UserProfile savedProfile = userProfileRepository.save(newProfile);

        Targets targets = new Targets(userId, null, null, null, null, null, true);
        targetCalculationService.applyAutoTargets(targets, savedProfile);
        targetsRepository.save(targets);

        if (userProfile.getWeight() != null) {
            bodyWeightRepository.save(new BodyWeight(null, userId, userProfile.getWeight(), LocalDate.now()));
        }

        log.info("Профиль создан | userId={}", savedProfile.getUserId());
        return savedProfile;
    }
}
