package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.profile.CreateUserProfileUseCase;
import com.example.fitness_assistant.application.service.profile.DeleteProfileUseCase;
import com.example.fitness_assistant.application.service.profile.FindProfileUseCase;
import com.example.fitness_assistant.application.service.profile.UpdateUserProfileUseCase;
import com.example.fitness_assistant.application.service.targets.UpdateTargetsUseCase;
import com.example.fitness_assistant.web.dto.request.create.CreateUserProfileRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateTargetsRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateUserProfileRequest;
import com.example.fitness_assistant.web.dto.response.TargetStatusResponse;
import com.example.fitness_assistant.web.dto.response.TargetsResponse;
import com.example.fitness_assistant.web.dto.response.UserProfileResponse;
import com.example.fitness_assistant.web.mapper.UserProfileWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UpdateTargetsUseCase updateTargetsUseCase;

    @GetMapping
    public UserProfileResponse getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userProfileWebMapper.toResponse(findProfileUseCase.findUserProfile(userDetails));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CreateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                createUserProfileUseCase.createUserProfile(userDetails,userProfileWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse updateProfile(@AuthenticationPrincipal UserDetails userDetails,@Valid @RequestBody UpdateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                updateUserProfileUseCase.updateUserProfile(userDetails,userProfileWebMapper.toDomain(request))
        );
    }
    @PatchMapping("/targets")
    @ResponseStatus(HttpStatus.OK)
    public TargetsResponse updateTargets(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateTargetsRequest request) {
        return userProfileWebMapper.toTargetsResponse(
                updateTargetsUseCase.updateTargets(userDetails, request)
        );
    }
    @GetMapping("/targets")
    public TargetsResponse getTarget(@AuthenticationPrincipal UserDetails userDetails) {
        return userProfileWebMapper.toTargetsResponse(findProfileUseCase.findUserProfile(userDetails));
    }
    @PatchMapping("/targets/auto")
    @ResponseStatus(HttpStatus.OK)
    public TargetStatusResponse updateAutopilotStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam boolean enabled) {
        return userProfileWebMapper.toTargetStatusResponse(
                updateUserProfileUseCase.updateAutopilotStatus(userDetails,enabled)
        );
    }



    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal UserDetails userDetails) {
        deleteProfileUseCase.deleteUserProfile(userDetails);
    }
}
