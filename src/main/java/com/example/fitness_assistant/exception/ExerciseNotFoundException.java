package com.example.fitness_assistant.exception;

public class ExerciseNotFoundException extends RuntimeException{
    public ExerciseNotFoundException(String name){
        super(name + " не найдено.");
    }

}
