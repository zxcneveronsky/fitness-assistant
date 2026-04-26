package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.web.dto.request.CreateUserProfileRequest;
import com.example.fitness_assistant.web.dto.request.UpdateUserProfileRequest;
import com.example.fitness_assistant.web.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileWebMapper {

    public UserProfile toDomain(CreateUserProfileRequest request) {
        return new UserProfile(
                request.userId(),
                null,
                request.name(),
                request.birthDate(),
                request.weight(),
                request.height(),
                request.gender()
        );
    }

    public UserProfile toDomain(UpdateUserProfileRequest request) {
        return new UserProfile(
                request.userId(),
                null,
                request.name(),
                request.birthDate(),
                request.weight(),
                request.height(),
                request.gender()
        );
    }

    public UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getName(),
                profile.getBirthDate(),
                profile.getWeight(),
                profile.getHeight(),
                profile.getGender()
        );
    }
}