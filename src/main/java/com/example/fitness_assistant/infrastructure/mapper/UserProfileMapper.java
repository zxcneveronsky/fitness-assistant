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
                null, // Это поле проставляется в адаптере через GetReferenceById
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

    public UserProfileEntity toEntity(UserProfile domain) {
        return new UserProfileEntity(
                domain.getId(),
                userMapper.toEntity(domain.getUser()),
                domain.getName(),
                domain.getBirthDate(),
                domain.getWeight(),
                domain.getHeight(),
                UserProfileEntity.Gender.valueOf(domain.getGender().name()),
                domain.getTargetKcal(),
                domain.getTargetProteins(),
                domain.getTargetFats(),
                domain.getTargetCarbs(),
                domain.getUseAutopilot()
        );

    }
}