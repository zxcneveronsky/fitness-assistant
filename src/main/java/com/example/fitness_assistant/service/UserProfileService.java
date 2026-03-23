package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.UserProfileDTO;
import com.example.fitness_assistant.entity.User;
import com.example.fitness_assistant.entity.UserProfile;
import com.example.fitness_assistant.repository.UserProfileRepository;
import com.example.fitness_assistant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileDTO getProfile(String email) {
        return userProfileRepository.findByUser_Email(email)
                .map(UserProfileDTO::fromEntity)
                .orElseGet(() -> UserProfileDTO.fromEntity(null));
    }

    @Transactional
    public UserProfileDTO saveProfile(String email, UserProfileDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserProfile profile = userProfileRepository
                .findByUser(user)
                .orElse(new UserProfile());

        profile.setUser(user);
        profile.setName(dto.name());
        profile.setBirthDate(dto.birthDate());
        profile.setWeight(dto.weight());
        profile.setHeight(dto.height());
        profile.setGender(dto.gender());

        userProfileRepository.save(profile);
        log.info("Профиль обновлён для: {}", email);
        return dto;
    }

    @Transactional
    public UserProfileDTO updateWeightHeight(String email, Double weight, Double height) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserProfile profile = userProfileRepository
                .findByUser(user)
                .orElse(new UserProfile());

        profile.setUser(user);
        if (weight != null) profile.setWeight(weight);
        if (height != null) profile.setHeight(height);

        userProfileRepository.save(profile);
        return getProfile(email);
    }
}