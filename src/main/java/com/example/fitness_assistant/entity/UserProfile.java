package com.example.fitness_assistant.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String name;

    private LocalDate birthDate;

    private Double weight;

    private Double height;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    public enum Gender {
        MALE, FEMALE
    }
}