package com.example.fitness_assistant.web.dto.request.update;

import com.example.fitness_assistant.core.model.UserProfile;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserProfileRequest(
        @Size(max = 255, message = "Имя слишком длинное")
        String name,

        @Past(message = "Дата рождения не может быть в будущем")
        LocalDate birthDate,

        @Min(value = 5, message = "Вес должен быть не менее 5 кг")
        @Max(value = 500, message = "Вес должен быть не более 500 кг")
        Double weight,

        @Min(value = 30, message = "Рост должен быть не менее 30 см")
        @Max(value = 300, message = "Рост должен быть не более 300 см")
        Double height,

        UserProfile.Gender gender
) { }
