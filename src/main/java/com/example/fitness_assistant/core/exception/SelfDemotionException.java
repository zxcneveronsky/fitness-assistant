package com.example.fitness_assistant.core.exception;

public class SelfDemotionException extends RuntimeException {
    public SelfDemotionException() {
        super("Нельзя понизить собственную роль до уровня USER.");
    }
}
