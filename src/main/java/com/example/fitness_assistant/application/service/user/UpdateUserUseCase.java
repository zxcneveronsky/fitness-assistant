package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserAlreadyExistsException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User updateUser(User userUpdate) {
        Long userId = userUpdate.getId();
        User updatedUser = userRepository.findById(userId)
                .map(existingUser -> {
                    if (userUpdate.getEmail() != null) {
                        if (!existingUser.getEmail().equals(userUpdate.getEmail())
                                && userRepository.existsByEmail(userUpdate.getEmail())) {
                            throw new UserAlreadyExistsException(userUpdate.getEmail());
                        }
                        existingUser.setEmail(userUpdate.getEmail());
                    }
                    if (userUpdate.getPassword() != null) {
                        existingUser.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
                    }
                    if (userUpdate.getRole() != null) {
                        existingUser.setRole(userUpdate.getRole());
                    }
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("Пользователь обновлён | id={}", userId);
        return updatedUser;
    }
}
