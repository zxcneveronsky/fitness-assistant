package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.UserProfile;
import com.example.fitness_assistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUser_Email(String email);
}