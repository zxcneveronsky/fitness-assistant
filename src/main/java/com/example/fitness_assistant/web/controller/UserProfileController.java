package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.profile.CreateUserProfileUseCase;
import com.example.fitness_assistant.application.service.profile.DeleteProfileUseCase;
import com.example.fitness_assistant.application.service.profile.FindProfileUseCase;
import com.example.fitness_assistant.application.service.profile.UpdateUserProfileUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateUserProfileRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateUserProfileRequest;
import com.example.fitness_assistant.web.dto.response.UserProfileResponse;
import com.example.fitness_assistant.web.mapper.UserProfileWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Validated
public class UserProfileController {

    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final FindProfileUseCase findProfileUseCase;
    private final DeleteProfileUseCase deleteProfileUseCase;
    private final UserProfileWebMapper userProfileWebMapper;

    @GetMapping
    public UserProfileResponse getProfile(@AuthenticationPrincipal UserDetailsAdapter adapter) {
        return userProfileWebMapper.toResponse(findProfileUseCase.findById(adapter.getUserId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createProfile(@AuthenticationPrincipal UserDetailsAdapter adapter, @Valid @RequestBody CreateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                createUserProfileUseCase.createUserProfile(adapter.getUserId(), userProfileWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse updateProfile(@AuthenticationPrincipal UserDetailsAdapter adapter, @Valid @RequestBody UpdateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                updateUserProfileUseCase.updateUserProfile(adapter.getUserId(), userProfileWebMapper.toDomain(request))
        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal UserDetailsAdapter adapter) {
        deleteProfileUseCase.deleteUserProfile(adapter.getUserId());
    }
}
