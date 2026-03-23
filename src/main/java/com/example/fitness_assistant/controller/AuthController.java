package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.AuthResponse;
import com.example.fitness_assistant.dto.LoginRequest;
import com.example.fitness_assistant.dto.RegisterRequest;
import com.example.fitness_assistant.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Регистрация и вход пользователей")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт аккаунт и возвращает JWT-токен. Токен действует 24 часа."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Email уже занят или данные невалидны",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Пользователь с таким email уже существует\"}")))
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(
            summary = "Вход в систему",
            description = "Проверяет email и пароль, возвращает JWT-токен."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный вход",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный email или пароль",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Неверный пароль\"}")))
    })
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}