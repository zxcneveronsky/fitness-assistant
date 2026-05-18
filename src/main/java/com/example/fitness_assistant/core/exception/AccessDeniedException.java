package com.example.fitness_assistant.core.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Доступ запрещён");
    }
}
