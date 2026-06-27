package com.example.fitness_assistant.core.exception;

public class MealNotFoundException extends RuntimeException {
    public MealNotFoundException(Long id) {
        super("Прием пищи с id " + id + " не найден.");
    }
}