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
        return new UserProfile(
                entity.getId(),
                userMapper.toDomain(entity.getUser()),
                entity.getName(),
                entity.getBirthDate(),
                entity.getWeight(),
                entity.getHeight(),
                UserProfile.Gender.valueOf(entity.getGender().name()),
                entity.getTargetKcal(),
                entity.getTargetProteins(),
                entity.getTargetFats(),
                entity.getTargetCarbs(),
                entity.getUseAutopilot()
        );
    }

    public UserProfileEntity toEntity(UserProfile userProfile) {
        return new UserProfileEntity(
                null,
                userMapper.toEntity(userProfile.getUser()),
                userProfile.getName(),
                userProfile.getBirthDate(),
                userProfile.getWeight(),
                userProfile.getHeight(),
                UserProfileEntity.Gender.valueOf(userProfile.getGender().name()),
                userProfile.getTargetKcal(),
                userProfile.getTargetProteins(),
                userProfile.getTargetFats(),
                userProfile.getTargetCarbs(),
                userProfile.getUseAutopilot()
        );

    }
}