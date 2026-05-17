package com.example.fitness_assistant.core.exception;

public class TargetsNotFoundException extends RuntimeException {
    public TargetsNotFoundException(Long id) {
        super("Цели пользователя с id " + id + " не найдены");
    }
}
