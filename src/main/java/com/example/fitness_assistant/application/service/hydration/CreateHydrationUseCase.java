package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.hydration.Hydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateHydrationUseCase {

    private final HydrationRepository hydrationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Hydration createHydration(UserDetails userDetails, Hydration hydration) {
        hydration.setId(null);
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        hydration.setUserId(userId);
        Hydration savedHydration = hydrationRepository.save(hydration);
        log.info("Запись гидратации создана | id={}", savedHydration.getId());
        return savedHydration;
    }
}
