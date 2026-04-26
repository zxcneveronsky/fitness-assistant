package com.example.fitness_assistant.core.exception;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(Long id) {
        super("Упражнение с id " + id + " не найдено.");
    }
}
