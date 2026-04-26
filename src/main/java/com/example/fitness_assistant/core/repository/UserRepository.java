package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.User;

import java.util.Optional;


public interface UserRepository {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    User getReferenceById(Long id);
}
