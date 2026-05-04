package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.Hydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateHydrationUseCase {

    private final HydrationRepository hydrationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Hydration createHydration(UserDetails userDetails, Hydration hydration) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        hydration.setUserId(userId);
        return hydrationRepository.save(hydration);
    }
}
