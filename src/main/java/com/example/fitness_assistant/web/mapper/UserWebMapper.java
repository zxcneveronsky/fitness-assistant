package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.application.service.user.LoginResult;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.web.dto.request.create.RegisterRequest;
import com.example.fitness_assistant.web.dto.response.AuthResponse;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public User toDomain(RegisterRequest request) {
        return  new User (
                null,
                request.email(),
                request.password(),
                User.Role.USER
        );
    }

    public AuthResponse toAuthResponse(String token, User user) {
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponse toAuthResponse(LoginResult result) {
        return new AuthResponse(
                result.token(),
                result.email(),
                result.role()
        );
    }
}
