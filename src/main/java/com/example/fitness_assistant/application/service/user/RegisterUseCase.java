package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserAlreadyExistsException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("Пользователь зарегистрирован | id={} | email='{}'", savedUser.getId(), savedUser.getEmail());
        return savedUser;
    }
}
