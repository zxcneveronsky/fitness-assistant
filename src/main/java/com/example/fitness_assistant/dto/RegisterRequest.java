package com.example.fitness_assistant.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "Некорректный email")
        @NotBlank(message = "Email не может быть пустым")
        String email,
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, message = "Пароль минимум 6 символов")
        String password
) {}