package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Email(message = "Некорректный email")
        @NotBlank(message = "Email не может быть пустым")
        @Size(max = 255, message = "Email слишком длинный")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, max = 255, message = "Пароль должен содержать от 6 до 255 символов")
        String password
) {}