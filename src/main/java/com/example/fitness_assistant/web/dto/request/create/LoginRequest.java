package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Email(message = "Некорректный email")
        @NotNull(message = "Email не может быть пустым")
        @NotBlank(message = "Email не может быть пустым")
        String email,

        @NotNull(message = "Пароль не может быть пустым")
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, max = 255, message = "Пароль от 6 до 255 символов")
        String password
) {}