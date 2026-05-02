package com.example.fitness_assistant.web.dto.request.create;

import com.example.fitness_assistant.core.model.UserProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserProfileRequest(
        @NotNull(message = "Имя не может быть пустым")
        @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 255, message = "Имя не должно превышать 255 символов")
        String name,

        @NotNull(message = "Дата рождения не может быть пустой")
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthDate,

        @NotNull(message = "Вес не может быть пустым")
        @Positive(message = "Вес должен быть положительным числом")
        Double weight,

        @NotNull(message = "Рост не может быть пустым")
        @Positive(message = "Рост должен быть положительным числом")
        Double height,

        @NotNull(message = "Пол не может быть пустым")
        UserProfile.Gender gender
) { }
