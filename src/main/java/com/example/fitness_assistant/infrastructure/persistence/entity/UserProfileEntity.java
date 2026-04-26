package com.example.fitness_assistant.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfileEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

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