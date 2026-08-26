package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.profile.CreateUserProfileUseCase;
import com.example.fitness_assistant.application.service.profile.DeleteUserProfileUseCase;
import com.example.fitness_assistant.application.service.profile.FindUserProfileUseCase;
import com.example.fitness_assistant.application.service.profile.UpdateUserProfileUseCase;
import com.example.fitness_assistant.application.service.streak.UpdateStreakUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateUserProfileRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateUserProfileRequest;
import com.example.fitness_assistant.web.dto.response.StreakResponse;
import com.example.fitness_assistant.web.dto.response.UserProfileResponse;
import com.example.fitness_assistant.web.mapper.StreakWebMapper;
import com.example.fitness_assistant.web.mapper.UserProfileWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Validated
public class UserProfileController {

    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final FindUserProfileUseCase findProfileUseCase;
    private final DeleteUserProfileUseCase deleteProfileUseCase;
    private final UserProfileWebMapper userProfileWebMapper;
    private final UpdateStreakUseCase updateStreakUseCase;
    private final StreakWebMapper streakWebMapper;

    @GetMapping
    public UserProfileResponse getProfile(@AuthenticationPrincipal UserDetailsAdapter adapter) {
        return userProfileWebMapper.toResponse(findProfileUseCase.findById(adapter.getUserId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createProfile(@AuthenticationPrincipal UserDetailsAdapter adapter, @Valid @RequestBody CreateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                createUserProfileUseCase.createUserProfile(adapter.getUserId(), userProfileWebMapper.toDomain(request), request.measuredAt())
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse updateProfile(@AuthenticationPrincipal UserDetailsAdapter adapter, @Valid @RequestBody UpdateUserProfileRequest request) {
        return userProfileWebMapper.toResponse(
                updateUserProfileUseCase.updateUserProfile(adapter.getUserId(), userProfileWebMapper.toDomain(request), request.measuredAt())
        );
    }

    @PostMapping("/streak")
    @ResponseStatus(HttpStatus.CREATED)
    public StreakResponse updateStreak(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam
            @NotNull(message = "Дата не может быть пустой")
            @PastOrPresent(message = "Дата не может быть в будущем")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return streakWebMapper.toResponse(updateStreakUseCase.updateStreak(adapter.getUserId(), date));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal UserDetailsAdapter adapter) {
        deleteProfileUseCase.deleteUserProfile(adapter.getUserId());
    }
}
