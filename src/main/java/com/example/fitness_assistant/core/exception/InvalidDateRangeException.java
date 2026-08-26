package com.example.fitness_assistant.core.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super("Дата начала не может быть позже даты окончания.");
    }
}
