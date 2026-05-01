package com.example.fitness_assistant.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "foods")
public class FoodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_seq")
    @SequenceGenerator(name = "food_seq", sequenceName = "food_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String brands;

    @Column(nullable = false)
    private Double kcal;

    @Column(nullable = false)
    private Double proteins;

    @Column(nullable = false)
    private Double fats;

    @Column(nullable = false)
    private Double carbs;
}