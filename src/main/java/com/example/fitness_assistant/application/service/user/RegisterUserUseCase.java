package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserAlreadyExistsException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.security.PasswordEncoder;
import com.example.fitness_assistant.core.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public LoginResult registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser);
        log.info("Пользователь зарегистрирован | id={} | email='{}'", savedUser.getId(), savedUser.getEmail());
        return new LoginResult(token, savedUser.getEmail(), savedUser.getRole().name());
    }
}
