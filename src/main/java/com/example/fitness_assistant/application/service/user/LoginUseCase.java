package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;
    // private final JwtTokenProvider jwtTokenProvider;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // if (!passwordEncoder.matches(password, user.getPassword())) {
        //     throw new RuntimeException("Invalid password"); // Consider a more specific exception here
        // }

        // return jwtTokenProvider.generateToken(user);
        return "token"; // Placeholder
    }
}
