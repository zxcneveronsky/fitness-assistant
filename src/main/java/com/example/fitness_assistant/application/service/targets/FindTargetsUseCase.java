package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.exception.TargetsNotFoundException;
import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindTargetsUseCase {

    private final TargetsRepository targetsRepository;

    public Targets findTargets(UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Targets targets = targetsRepository.findById(userId)
                .orElseThrow(() -> new TargetsNotFoundException(userId));
        log.info("Цели найдены | userId={}", userId);
        return targets;
    }
}
