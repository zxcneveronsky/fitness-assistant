package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.InvalidPasswordException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
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
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public LoginResult loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = tokenProvider.generateToken(user);
        log.info("Пользователь вошёл в систему | id={} | email='{}'", user.getId(), user.getEmail());
        return new LoginResult(token, user.getEmail(), user.getRole().name());
    }
}
