package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.model.Hydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FindHydrationUseCase {

    private final HydrationRepository hydrationRepository;

    public Page<Hydration> findHydration(LocalDateTime localDateTime, UserDetails userDetails, Pageable pageable) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        return hydrationRepository.searchHydration(localDateTime, userId, pageable);
    }
}
