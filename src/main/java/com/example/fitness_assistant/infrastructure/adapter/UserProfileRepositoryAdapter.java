package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.mapper.UserProfileMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.UserProfileEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserProfileRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepository {

    private final JpaUserProfileRepository jpaUserProfileRepository;
    private final JpaUserRepository jpaUserRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public Optional<UserProfile> findById(Long id) {
        return jpaUserProfileRepository.findById(id)
                .map(userProfileMapper::toDomain);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        UserProfileEntity userProfileEntity = userProfileMapper.toEntity(userProfile);
        userProfileEntity.setUser(jpaUserRepository.getReferenceById(userProfile.getId()));
        UserProfileEntity saved = jpaUserProfileRepository.save(userProfileEntity);
        return userProfileMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserProfileRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaUserProfileRepository.existsById(id);
    }
}
