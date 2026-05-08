package com.example.fitness_assistant.core.exception;

public class SetNotFoundException extends RuntimeException {
    public SetNotFoundException(Long id) {
        super("Подход с id " + id + " не найден.");
    }
}
