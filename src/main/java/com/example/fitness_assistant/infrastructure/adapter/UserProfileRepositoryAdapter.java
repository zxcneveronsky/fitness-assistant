package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.core.repository.UserProfileRepository;
import com.example.fitness_assistant.infrastructure.mapper.UserProfileMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.UserProfileEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserProfileRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepository {

    private final JpaUserProfileRepository jpaUserProfileRepository;
    private final JpaUserRepository jpaUserRepository;
    private final UserProfileMapper userProfileMapper;
    private final EntityManager entityManager;

    @Override
    @Cacheable("userProfiles")
    public Optional<UserProfile> findById(Long id) {
        return jpaUserProfileRepository.findById(id)
                .map(userProfileMapper::toDomain);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userProfiles", allEntries = true)
    public UserProfile save(UserProfile userProfile) {
        UserProfileEntity entity = userProfileMapper.toEntity(userProfile);
        entity.setUser(jpaUserRepository.getReferenceById(userProfile.getId()));
        if (!jpaUserProfileRepository.existsById(userProfile.getId())) {
            entityManager.persist(entity);
            return userProfileMapper.toDomain(entity);
        } else {
            UserProfileEntity merged = entityManager.merge(entity);
            return userProfileMapper.toDomain(merged);
        }
    }

    @Override
    @CacheEvict(value = "userProfiles", allEntries = true)
    public void deleteById(Long id) {
        jpaUserProfileRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaUserProfileRepository.existsById(id);
    }
}
