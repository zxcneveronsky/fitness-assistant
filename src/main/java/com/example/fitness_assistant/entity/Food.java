package com.example.fitness_assistant.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "food")
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String barcode;
    @Column(nullable = false)
    private String name;
    private String brands;
    private double kcal;
    private double proteins;
    private double fats;
    private double carbs;
}
