package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserAlreadyExistsException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public LoginResult registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        log.info("Пользователь зарегистрирован | id={} | email='{}'", savedUser.getId(), savedUser.getEmail());
        return new LoginResult(token, savedUser.getEmail(), savedUser.getRole().name());
    }
}
