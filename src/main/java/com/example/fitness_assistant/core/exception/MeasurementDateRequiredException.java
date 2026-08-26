package com.example.fitness_assistant.core.exception;

public class MeasurementDateRequiredException extends RuntimeException {
    public MeasurementDateRequiredException() {
        super("Дата взвешивания должна быть указана вместе с весом.");
    }
}
