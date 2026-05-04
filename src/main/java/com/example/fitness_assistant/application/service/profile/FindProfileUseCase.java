package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProfileUseCase {

    private final UserProfileRepository userProfileRepository;

    public UserProfile findUserProfile(UserDetails userDetails) {
        Long id = ((UserDetailsAdapter) userDetails).getUserId();
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException(id));
    }
}
