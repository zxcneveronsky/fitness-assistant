package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.AuthResponse;
import com.example.fitness_assistant.dto.LoginRequest;
import com.example.fitness_assistant.dto.RegisterRequest;
import com.example.fitness_assistant.entity.User;
import com.example.fitness_assistant.exception.AuthException;
import com.example.fitness_assistant.exception.UserAlreadyExistsException;
import com.example.fitness_assistant.repository.UserRepository;
import com.example.fitness_assistant.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(User.Role.USER);
        userRepository.save(user);

        log.info("Регистрация: {}", user.getEmail());
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        return userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map(user -> {
                    log.info("Вход выполнен: {}", user.getEmail());
                    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
                    return new AuthResponse(token, user.getEmail(), user.getRole().name());
                })
                .orElseThrow(() -> new AuthException("Неверный логин или пароль"));
    }
}