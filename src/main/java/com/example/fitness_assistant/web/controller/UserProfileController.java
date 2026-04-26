package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.profile.CreateUserProfileUseCase;
import com.example.fitness_assistant.application.service.profile.DeleteProfileUseCase;
import com.example.fitness_assistant.application.service.profile.FindProfileUseCase;
import com.example.fitness_assistant.application.service.profile.UpdateUserProfileUseCase;
import com.example.fitness_assistant.web.dto.request.CreateUserProfileRequest;
import com.example.fitness_assistant.web.dto.request.UpdateUserProfileRequest;
import com.example.fitness_assistant.web.dto.response.UserProfileResponse;
import com.example.fitness_assistant.web.mapper.UserProfileWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final FindProfileUseCase findProfileUseCase;
    private final UserProfileWebMapper userProfileWebMapper;
    private final DeleteProfileUseCase deleteProfileUseCase;

    @GetMapping("/{userId}")
    public UserProfileResponse getProfile(@PathVariable Long userId) {
        return userProfileWebMapper.toResponse(findProfileUseCase.findById(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createProfile(@Valid @RequestBody CreateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                createUserProfileUseCase.saveProfile(userProfileWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse updateProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                updateUserProfileUseCase.updateUserProfile(userProfileWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable Long id) {
        deleteProfileUseCase.deleteById(id);
    }
}
