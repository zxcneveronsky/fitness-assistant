package com.example.fitness_assistant.core.exception;

public class MuscleNotFoundException extends RuntimeException {
    public MuscleNotFoundException(Long id) {
        super("Мышца с id " + id + " не найдена.");
    }
}
