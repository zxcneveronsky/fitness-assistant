package com.example.fitness_assistant.core.exception;

public class FavoriteFoodNotFoundException extends RuntimeException {
    public FavoriteFoodNotFoundException(Long foodId) {
        super("Продукт с id " + foodId + " не найден в избранном.");
    }
}
