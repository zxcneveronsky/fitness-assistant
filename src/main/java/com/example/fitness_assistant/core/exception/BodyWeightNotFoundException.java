package com.example.fitness_assistant.core.exception;

public class BodyWeightNotFoundException extends RuntimeException {
    public BodyWeightNotFoundException(Long id) {
        super("Запись веса не найдена: " + id);
    }
}
