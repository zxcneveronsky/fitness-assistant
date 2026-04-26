package com.example.fitness_assistant.web.dto.response;

import com.example.fitness_assistant.core.model.UserProfile.Gender;

import java.time.LocalDate;

public record UserProfileResponse(
        String name,
        LocalDate birthDate,
        Double weight,
        Double height,
        Gender gender
) { }