package com.example.fitness_assistant.web.dto.request.update;

import com.example.fitness_assistant.core.model.UserProfile;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateUserProfileRequest(
        //@NotBlank(message = "Имя не может быть пустым")
        @Size(max = 255, message = "Имя не должно превышать 255 символов")
        String name,

        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthDate,

        @Positive(message = "Вес должен быть положительным числом")
        Double weight,

        @Positive(message = "Рост должен быть положительным числом")
        Double height,

        UserProfile.Gender gender
) { }
