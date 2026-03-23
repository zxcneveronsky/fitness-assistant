package com.example.fitness_assistant.dto;

import com.example.fitness_assistant.entity.UserProfile.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Профиль пользователя с персональными параметрами")
public record UserProfileDTO(

        @Schema(description = "Имя пользователя", example = "Григорий")
        String name,

        @Schema(description = "Дата рождения в формате YYYY-MM-DD", example = "2009-12-05")
        LocalDate birthDate,

        @Schema(description = "Вес в килограммах", example = "75.5")
        Double weight,

        @Schema(description = "Рост в сантиметрах", example = "180.0")
        Double height,

        @Schema(description = "Пол пользователя", example = "MALE", allowableValues = {"MALE", "FEMALE"})
        Gender gender
) {}