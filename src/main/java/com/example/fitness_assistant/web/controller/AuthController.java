package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.user.DeleteUserUseCase;
import com.example.fitness_assistant.application.service.user.LoginResult;
import com.example.fitness_assistant.application.service.user.LoginUseCase;
import com.example.fitness_assistant.application.service.user.RegisterUseCase;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserWebMapper userWebMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        LoginResult loginResult = registerUseCase.register(userWebMapper.toDomain(request));
        return userWebMapper.toAuthResponse(loginResult);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        LoginResult loginResult = loginUseCase.login(request.email(), request.password());
        return userWebMapper.toAuthResponse(loginResult);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetailsAdapter adapter) {
        if (!adapter.getUserId().equals(id)) {
            throw new AccessDeniedException();
        }
        deleteUserUseCase.deleteUser(id);
    }
}
