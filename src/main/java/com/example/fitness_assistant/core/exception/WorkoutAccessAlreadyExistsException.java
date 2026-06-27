package com.example.fitness_assistant.core.exception;

public class WorkoutAccessAlreadyExistsException extends RuntimeException {
    public WorkoutAccessAlreadyExistsException(Long workoutId) {
        super("Доступ к тренировке с id " + workoutId + " уже существует.");
    }
}
