package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        if (userProfileRepository.existsById(id)){
            userProfileRepository.deleteById(id);
        }
        userRepository.deleteById(id);
    }
}
