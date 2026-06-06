package com.example.fitness_assistant.core.exception;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException() {
        super("Профиль пользователя не найден.");
    }
}
