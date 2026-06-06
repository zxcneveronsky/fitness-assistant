package com.example.fitness_assistant.core.exception;

public class FavoriteExerciseNotFoundException extends RuntimeException {
    public FavoriteExerciseNotFoundException(Long exerciseId) {
        super("Упражнение с id " + exerciseId + " не найдено в избранном.");
    }
}
