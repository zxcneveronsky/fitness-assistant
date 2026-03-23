package com.example.fitness_assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для входа")
public record LoginRequest(

        @Schema(description = "Email пользователя", example = "user@example.com")
        @Email
        @NotBlank
        String email,

        @Schema(description = "Пароль", example = "secret123")
        @NotBlank
        String password
) {}