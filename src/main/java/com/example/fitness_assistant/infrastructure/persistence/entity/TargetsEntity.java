package com.example.fitness_assistant.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "targets")
public class TargetsEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "profile_id")
    private UserProfileEntity profile;

    @Column(name = "target_kcal")
    private Double targetKcal;

    @Column(name = "target_proteins")
    private Double targetProteins;

    @Column(name = "target_fats")
    private Double targetFats;

    @Column(name = "target_carbs")
    private Double targetCarbs;

    @Column(name = "target_hydration")
    private Double targetHydration;

    @Column(name = "use_autopilot")
    private Boolean useAutopilot = true;
}
