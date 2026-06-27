package com.example.fitness_assistant.core.model.workout;

import com.example.fitness_assistant.core.model.exercise.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutWithExercise {
    private Long id;
    private Long userId;
    private String name;
    private List<Exercise> exercises;
}