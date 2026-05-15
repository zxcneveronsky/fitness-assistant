package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindProfileUseCase {

    private final UserProfileRepository userProfileRepository;

    public UserProfile findUserProfile(UserDetails userDetails) {
        Long id = ((UserDetailsAdapter) userDetails).getUserId();
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException(id));
        log.info("Профиль найден | userId={}", id);
        return profile;
    }
}
