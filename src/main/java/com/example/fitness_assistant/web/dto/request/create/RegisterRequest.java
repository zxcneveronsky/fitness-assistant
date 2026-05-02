package com.example.fitness_assistant.web.dto.request.create;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "Некорректный email")
        @NotNull(message = "Email не может быть пустым")
        @NotBlank(message = "Email не может быть пустым")
        String email,
        @NotNull(message = "Пароль не может быть пустым")
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, message = "Пароль минимум 6 символов")
        String password
) {}