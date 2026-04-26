package com.example.fitness_assistant.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "muscles")
public class MuscleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "muscle_seq")
    @SequenceGenerator(name = "muscle_seq", sequenceName = "muscle_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}