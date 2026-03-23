package com.example.fitness_assistant.dto;

import com.example.fitness_assistant.entity.UserProfile.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserProfileDTO(

        String name,
        LocalDate birthDate,
        Double weight,
        Double height,
        Gender gender
) {
    public static UserProfileDTO fromEntity(com.example.fitness_assistant.entity.UserProfile p) {
        if (p == null) return new UserProfileDTO(null, null, null, null, null);
        return new UserProfileDTO(p.getName(), p.getBirthDate(), p.getWeight(), p.getHeight(), p.getGender());
    }
}