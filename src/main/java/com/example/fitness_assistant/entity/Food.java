package com.example.fitness_assistant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String brands;

    @Column(nullable = false)
    private double kcal;

    @Column(nullable = false)
    private double proteins;

    @Column(nullable = false)
    private double fats;

    @Column(nullable = false)
    private double carbs;

    public Food() {}
}