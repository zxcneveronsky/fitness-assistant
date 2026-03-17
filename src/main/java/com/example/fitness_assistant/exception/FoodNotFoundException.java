package com.example.fitness_assistant.exception;

public class FoodNotFoundException extends RuntimeException{
    public FoodNotFoundException(String name){
        super(name + " не найдено.");
    }
}
