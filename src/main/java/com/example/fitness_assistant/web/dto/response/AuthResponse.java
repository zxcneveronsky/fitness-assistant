package com.example.fitness_assistant.web.dto.response;

public record AuthResponse(
        String token,
        String email,
        String role
) {}