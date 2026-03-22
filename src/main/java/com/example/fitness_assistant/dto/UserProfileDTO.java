package com.example.fitness_assistant.dto;

import java.time.LocalDate;

public record UserProfileDTO(
        String name,
        LocalDate birthDate,
        Double weight,
        Double height
) {}