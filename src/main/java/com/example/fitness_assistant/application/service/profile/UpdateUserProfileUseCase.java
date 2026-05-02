package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.application.service.targets.TargetCalculationService;
import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserProfileRepository userProfileRepository;
    private final TargetCalculationService targetCalculationService;

    @Transactional
    public UserProfile updateUserProfile(UserDetails userDetails, UserProfile profileUpdate) {
        Long id = ((UserDetailsAdapter) userDetails).getUserId();
        return userProfileRepository.findById(id)
                .map(existingProfile -> {
                    existingProfile.setName(profileUpdate.getName() != null ? profileUpdate.getName() : existingProfile.getName());
                    existingProfile.setBirthDate(profileUpdate.getBirthDate() != null ? profileUpdate.getBirthDate() : existingProfile.getBirthDate());
                    existingProfile.setWeight(profileUpdate.getWeight() != null ? profileUpdate.getWeight() : existingProfile.getWeight());
                    existingProfile.setHeight(profileUpdate.getHeight() != null ? profileUpdate.getHeight() : existingProfile.getHeight());
                    existingProfile.setGender(profileUpdate.getGender() != null ? profileUpdate.getGender() : existingProfile.getGender());
                    return userProfileRepository.save(targetCalculationService.applyAutoTargets(existingProfile));
                })
                .orElseThrow(() -> new UserProfileNotFoundException(id));
    }
}
