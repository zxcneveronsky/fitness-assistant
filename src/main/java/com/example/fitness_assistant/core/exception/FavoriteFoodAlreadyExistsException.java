package com.example.fitness_assistant.core.exception;

public class FavoriteFoodAlreadyExistsException extends RuntimeException {
    public FavoriteFoodAlreadyExistsException(Long foodId) {
        super("Продукт с id " + foodId + " уже добавлен в избранное.");
    }
}
