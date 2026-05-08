package com.example.fitness_assistant.core.model.workout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workout {
    private Long id;
    private Long userId;
    private String name;
    private List<Long> exercisesIds;
}
