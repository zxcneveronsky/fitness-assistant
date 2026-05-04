package com.example.fitness_assistant.core.exception;

public class HydrationNotFoundException extends RuntimeException {
    public HydrationNotFoundException(Long id) {
        super("Прием воды с id " + id + " не найден.");
    }
}
