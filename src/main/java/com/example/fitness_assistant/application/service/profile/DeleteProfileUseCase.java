package com.example.fitness_assistant.application.service.profile;

import com.example.fitness_assistant.core.exception.UserProfileNotFoundException;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteProfileUseCase {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void deleteById(UserDetails userDetails) {
        Long id = ((UserDetailsAdapter) userDetails).getUserId();

        if (!userProfileRepository.existsById(id)) {
            throw new UserProfileNotFoundException(id);
        }
        userProfileRepository.deleteById(id);
    }
}
