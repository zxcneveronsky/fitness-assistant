package com.example.fitness_assistant.core.repository;

import com.example.fitness_assistant.core.model.UserProfile;


import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findById(Long id);
    UserProfile save(UserProfile userProfile);
    void deleteById(Long id);
    boolean existsById(Long id);

}
