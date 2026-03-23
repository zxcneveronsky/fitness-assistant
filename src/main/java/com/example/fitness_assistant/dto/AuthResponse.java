package com.example.fitness_assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ при успешной аутентификации")
public record AuthResponse(

        @Schema(description = "JWT-токен для авторизации запросов", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Email пользователя", example = "user@example.com")
        String email,

        @Schema(description = "Роль пользователя", example = "USER", allowableValues = {"USER", "ADMIN"})
        String role
) {}