package com.example.fitness_assistant.web.dto.response;

import com.example.fitness_assistant.core.model.User.Role;

public record UserResponse(
        Long id,
        String email,
        Role role
) {}
