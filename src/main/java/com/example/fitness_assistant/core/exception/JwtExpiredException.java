package com.example.fitness_assistant.core.exception;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException() {
        super("Срок действия токена истёк.");
    }
}
