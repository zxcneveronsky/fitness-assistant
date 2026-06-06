package com.example.fitness_assistant.core.exception;

public class FavoriteExerciseAlreadyExistsException extends RuntimeException {
    public FavoriteExerciseAlreadyExistsException(Long exerciseId) {
        super("Упражнение с id " + exerciseId + " уже добавлено в избранное.");
    }
}
