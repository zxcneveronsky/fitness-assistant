package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserProfileUseCase {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserProfile saveProfile(UserProfile userProfile) {
        Long userId = userProfile.getId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        User user = userRepository.getReferenceById(userProfile.getId());
        userProfile.setUser(user);
        userProfile.setId(null);
        return userProfileRepository.save(userProfile);
    }
}
