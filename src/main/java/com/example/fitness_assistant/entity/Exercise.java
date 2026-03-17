package com.example.fitness_assistant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Table(name = "exercise")
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "Название Группы Мышц не может быть пустым")
    private String muscleGroup;
    @Column(nullable = false)
    @NotBlank(message = "Название Мышцы не может быть пустым")
    private String muscleDetail;
    @Column(nullable = false)
    @NotBlank(message = "Название Упражнения не может быть пустым")
    private String exerciseName;
    private String description;
}
