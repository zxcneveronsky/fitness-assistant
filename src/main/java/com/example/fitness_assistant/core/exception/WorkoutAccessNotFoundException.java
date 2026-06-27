package com.example.fitness_assistant.core.exception;

public class WorkoutAccessNotFoundException extends RuntimeException {
    public WorkoutAccessNotFoundException(Long id) {
        super("Доступ к тренировке не найден | id=" + id);
    }
}
