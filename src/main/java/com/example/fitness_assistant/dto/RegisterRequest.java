package com.example.fitness_assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для регистрации")
public record RegisterRequest(

        @Schema(description = "Email пользователя", example = "user@example.com")
        @Email(message = "Некорректный email")
        @NotBlank(message = "Email не может быть пустым")
        String email,

        @Schema(description = "Пароль — минимум 6 символов", example = "secret123")
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, message = "Пароль минимум 6 символов")
        String password
) {}