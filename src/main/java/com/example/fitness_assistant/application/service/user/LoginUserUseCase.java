package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.InvalidPasswordException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
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
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResult loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtService.generateToken(user);
        log.info("Пользователь вошёл в систему | id={} | email='{}'", user.getId(), user.getEmail());
        return new LoginResult(token, user.getEmail(), user.getRole().name());
    }
}