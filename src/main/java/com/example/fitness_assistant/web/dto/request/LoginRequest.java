package com.example.fitness_assistant.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(

        @Email(message = "Некорректный email")
        @NotNull(message = "Email не может быть пустым")
        @NotBlank(message = "Email не может быть пустым")
        String email,

        @NotNull(message = "Пароль не может быть пустым")
        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {}