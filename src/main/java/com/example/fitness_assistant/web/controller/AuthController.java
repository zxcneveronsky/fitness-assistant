package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.user.DeleteUserUseCase;
import com.example.fitness_assistant.application.service.user.LoginUseCase;
import com.example.fitness_assistant.application.service.user.RegisterUseCase;
import com.example.fitness_assistant.web.dto.response.AuthResponse;
import com.example.fitness_assistant.web.dto.request.create.LoginRequest;
import com.example.fitness_assistant.web.dto.request.create.RegisterRequest;
import com.example.fitness_assistant.web.mapper.UserWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        var user = registerUseCase.register(userWebMapper.toDomain(request));
        String token = loginUseCase.login(request.email(), request.password());
        return userWebMapper.toAuthResponse(token, user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        String token = loginUseCase.login(request.email(), request.password());
        return new AuthResponse(token, request.email(), "USER");
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteUser(id);
    }
}