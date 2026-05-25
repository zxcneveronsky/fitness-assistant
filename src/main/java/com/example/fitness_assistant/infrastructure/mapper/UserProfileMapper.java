package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.infrastructure.persistence.entity.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    private final UserMapper userMapper;

    public UserProfile toDomain(UserProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserProfile(
                entity.getId(),
                null, // Это поле проставляется в адаптере через getReferenceById
                entity.getName(),
                entity.getBirthDate(),
                entity.getWeight(),
                entity.getHeight(),
                UserProfile.Gender.valueOf(entity.getGender().name())
        );
    }

    public UserProfileEntity toEntity(UserProfile domain) {
        if (domain == null) {
            return null;
        }
        return new UserProfileEntity(
                domain.getId(),
                null, // Это поле проставляется в адаптере через getReference
                domain.getName(),
                domain.getBirthDate(),
                domain.getWeight(),
                domain.getHeight(),
                UserProfileEntity.Gender.valueOf(domain.getGender().name())
        );

    }
}
