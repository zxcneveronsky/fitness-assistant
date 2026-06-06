package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteProfileUseCase {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void deleteUserProfile(Long userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new UserProfileNotFoundException();
        }
        userProfileRepository.deleteById(userId);
        log.info("Профиль удалён | userId={}", userId);
    }
}
