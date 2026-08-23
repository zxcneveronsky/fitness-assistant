package com.example.fitness_assistant.web.dto.request.create;

import com.example.fitness_assistant.core.model.UserProfile;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserProfileRequest(
        @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 255, message = "Имя слишком длинное")
        String name,

        @NotNull(message = "Дата рождения должна быть указана")
        @Past(message = "Дата рождения не может быть в будущем")
        LocalDate birthDate,

        @NotNull(message = "Вес не может быть пустым")
        @Min(value = 5, message = "Вес должен быть не менее 5 кг")
        @Max(value = 500, message = "Вес должен быть не более 500 кг")
        Double weight,

        @NotNull(message = "Рост не может быть пустым")
        @Min(value = 30, message = "Рост должен быть не менее 30 см")
        @Max(value = 300, message = "Рост должен быть не более 300 см")
        Double height,

        @NotNull(message = "Пол не может быть пустым")
        UserProfile.Gender gender
) { }
