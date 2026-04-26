package com.example.fitness_assistant.core.exception;

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException(Long id) {
        super("Еда с id " + id + " не найдена.");
    }
}
