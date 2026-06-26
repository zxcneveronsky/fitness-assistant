package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.user.DeleteUserUseCase;
import com.example.fitness_assistant.application.service.user.LoginResult;
import com.example.fitness_assistant.application.service.user.LoginUserUseCase;
import com.example.fitness_assistant.application.service.user.RegisterUserUseCase;
import com.example.fitness_assistant.core.exception.AccessDeniedException;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.response.AuthResponse;
import com.example.fitness_assistant.web.dto.request.create.LoginRequest;
import com.example.fitness_assistant.web.dto.request.create.RegisterRequest;
import com.example.fitness_assistant.web.mapper.UserWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final RegisterUserUseCase registerUseCase;
    private final LoginUserUseCase loginUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserWebMapper userWebMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        LoginResult loginResult = registerUseCase.registerUser(userWebMapper.toDomain(request));
        return userWebMapper.toAuthResponse(loginResult);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        LoginResult loginResult = loginUseCase.loginUser(request.email(), request.password());
        return userWebMapper.toAuthResponse(loginResult);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal UserDetailsAdapter adapter,
                           @PathVariable("id") Long userId) {
        if (!adapter.getUserId().equals(userId)) {
            throw new AccessDeniedException();
        }
        deleteUserUseCase.deleteUser(userId);
    }
}
