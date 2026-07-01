package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.application.service.user.LoginResult;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.web.dto.request.create.RegisterRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateUserRequest;
import com.example.fitness_assistant.web.dto.response.AuthResponse;
import com.example.fitness_assistant.web.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public User toDomain(RegisterRequest request) {
        return  new User (
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.email(),
                request.password(),
                User.Role.USER
        );
    }

    public User toDomain(UpdateUserRequest request) {
        return new User(
                request.id(),
                request.email(),
                request.password(),
                request.role()
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
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
