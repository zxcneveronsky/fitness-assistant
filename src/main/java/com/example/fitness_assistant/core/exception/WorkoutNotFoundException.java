package com.example.fitness_assistant.core.exception;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(Long id) {
        super("Тренировка с id " + id + " не найдена.");
    }
}
