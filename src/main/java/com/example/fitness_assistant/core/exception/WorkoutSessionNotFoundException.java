package com.example.fitness_assistant.core.exception;

public class WorkoutSessionNotFoundException extends RuntimeException {
    public WorkoutSessionNotFoundException(Long id) {
        super("Сессия тренировки с id " + id + " не найдена.");
    }
}
