package com.example.fitness_assistant.core.exception;

public class JwtUserNotFoundException extends RuntimeException {
    public JwtUserNotFoundException(String email) {
        super("Пользователь с email " + email + " из токена не найден.");
    }
}
