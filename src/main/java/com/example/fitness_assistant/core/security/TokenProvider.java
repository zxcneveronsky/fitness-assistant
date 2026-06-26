package com.example.fitness_assistant.core.security;

import com.example.fitness_assistant.core.model.User;

public interface TokenProvider {
    String generateToken(User user);
}
