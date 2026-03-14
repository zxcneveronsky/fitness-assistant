package com.example.fitness_assistant.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Table(name = "exercise")
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String muscleGroup;
    @Column(nullable = false)
    private String muscleDetail;
    @Column(nullable = false)
    private String exerciseName;
    private String description;
}
