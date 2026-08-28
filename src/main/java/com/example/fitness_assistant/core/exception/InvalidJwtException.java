package com.example.fitness_assistant.core.exception;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException() {
        super("Невалидный токен.");
    }
}
