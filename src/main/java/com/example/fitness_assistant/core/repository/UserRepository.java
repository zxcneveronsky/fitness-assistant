package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    User save(User user);
    void deleteById(Long id);
    User getReferenceById(Long id);
}
