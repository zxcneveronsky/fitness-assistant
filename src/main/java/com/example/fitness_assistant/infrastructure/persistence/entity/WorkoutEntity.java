package com.example.fitness_assistant.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "workouts")
public class WorkoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @OrderColumn(name = "exercise_order")
    @BatchSize(size = 15)
    @CollectionTable(
            name = "workout_exercises",
            joinColumns = @JoinColumn(name = "workout_id")
    )
    @Column(name = "exercise_id")
    private List<Long> exerciseIds;

}
