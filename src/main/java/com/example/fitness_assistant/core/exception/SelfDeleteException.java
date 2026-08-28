package com.example.fitness_assistant.core.exception;

public class SelfDeleteException extends RuntimeException {
    public SelfDeleteException() {
        super("Нельзя удалить собственный аккаунт с ролью уровня ADMIN.");
    }
}
