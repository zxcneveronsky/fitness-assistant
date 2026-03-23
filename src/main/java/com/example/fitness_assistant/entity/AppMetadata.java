package com.example.fitness_assistant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "app_metadata")
@NoArgsConstructor
@AllArgsConstructor
public class AppMetadata {

    @Id
    @Column(length = 100)
    private String key;

    @Column(nullable = false, length = 255)
    private String value;
}