package com.example.fitness_assistant.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exercise_muscle")
public class ExerciseMuscle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private String muscleGroup;

    @Column(nullable = false)
    private String muscleDetail;
}