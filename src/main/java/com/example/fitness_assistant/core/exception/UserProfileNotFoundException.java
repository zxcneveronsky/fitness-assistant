package com.example.fitness_assistant.core.exception;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(Long id) {
        super("Профиль пользователя с id " + id + " не найден.");
    }
}
