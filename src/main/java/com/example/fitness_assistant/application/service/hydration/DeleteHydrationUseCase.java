package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.HydrationNotFoundException;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteHydrationUseCase {

    private final HydrationRepository hydrationRepository;

    @Transactional
    public void deleteHydration(Long id, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!hydrationRepository.existsById(id, userId)) {
            throw new HydrationNotFoundException(id);
        }
        hydrationRepository.deleteById(id, userId);
    }
}
