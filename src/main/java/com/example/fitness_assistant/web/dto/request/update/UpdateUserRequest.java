package com.example.fitness_assistant.web.dto.request.update;

import com.example.fitness_assistant.core.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotNull(message = "ID пользователя не может быть пустым")
        Long id,
        @Email(message = "Некорректный email")
        @Size(max = 255, message = "Email слишком длинный")
        String email,
        @Size(min = 6, max = 255, message = "Пароль минимум 6 символов")
        String password,
        User.Role role
) {}
