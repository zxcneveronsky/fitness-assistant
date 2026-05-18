package com.example.fitness_assistant.core.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Неверный пароль");
    }
}
