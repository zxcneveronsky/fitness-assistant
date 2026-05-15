package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
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
public class DeleteProfileUseCase {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void deleteUserProfile(UserDetails userDetails) {
        Long id = ((UserDetailsAdapter) userDetails).getUserId();

        if (!userProfileRepository.existsById(id)) {
            throw new UserProfileNotFoundException(id);
        }
        userProfileRepository.deleteById(id);
        log.info("Профиль удалён | userId={}", id);
    }
}
