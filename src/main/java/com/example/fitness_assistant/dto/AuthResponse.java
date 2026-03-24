package com.example.fitness_assistant.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {}