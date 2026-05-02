package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                User.Role.valueOf(entity.getRole().name())
        );
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return new UserEntity(
                domain.getId(),
                domain.getEmail(),
                domain.getPassword(),
                UserEntity.Role.valueOf(domain.getRole().name())
        );
    }
}