package com.example.fitness_assistant.core.exception;

public class TargetsNotFoundException extends RuntimeException {
    public TargetsNotFoundException() {
        super("Цели пользователя не найдены.");
    }
}
