package com.example.fitness_assistant.web.handler;

import com.example.fitness_assistant.core.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            HydrationNotFoundException.class,
            MealNotFoundException.class,
            ExerciseNotFoundException.class,
            FoodNotFoundException.class,
            UserNotFoundException.class,
            UserProfileNotFoundException.class,
            WorkoutNotFoundException.class,
            WorkoutSessionNotFoundException.class,
            SetNotFoundException.class,
            TargetsNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFoundExceptions(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPassword(InvalidPasswordException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}