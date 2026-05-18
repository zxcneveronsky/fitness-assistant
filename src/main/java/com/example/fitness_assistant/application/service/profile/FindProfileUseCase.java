package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindProfileUseCase {

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public UserProfile findUserProfile(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        log.info("Профиль найден | userId={}", userId);
        return profile;
    }
}
