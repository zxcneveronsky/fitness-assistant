package com.example.fitness_assistant.core.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Long id;
    private User user;
    private String name;
    private LocalDate birthDate;
    private Double weight;
    private Double height;
    private Gender gender;
    private Double targetKcal;
    private Double targetProteins;
    private Double targetFats;
    private Double targetCarbs;
    private Boolean useAutopilot;

    public enum Gender {
        MALE, FEMALE
    }
}