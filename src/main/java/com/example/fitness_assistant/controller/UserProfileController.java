package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.UserProfileDTO;
import com.example.fitness_assistant.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public UserProfileDTO getProfile(Authentication auth) {
        return userProfileService.getProfile(auth.getName());
    }

    @PostMapping
    public UserProfileDTO saveProfile(Authentication auth,
                                      @RequestBody UserProfileDTO dto) {
        return userProfileService.saveProfile(auth.getName(), dto);
    }

    @PatchMapping("/body")
    public UserProfileDTO updateBody(Authentication auth,
                                     @RequestBody Map<String, Double> body) {
        return userProfileService.updateWeightHeight(
                auth.getName(),
                body.get("weight"),
                body.get("height")
        );
    }
}