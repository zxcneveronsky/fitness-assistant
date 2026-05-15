package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.hydration.DailyHydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetDailyHydrationUseCase {
    private final HydrationRepository hydrationRepository;
    private final UserRepository userRepository;

    public DailyHydration getDailyHydration(LocalDateTime localDateTime, UserDetails userDetails){
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        DailyHydration dailyHydration = hydrationRepository.getDailyWater(localDateTime,userId);
        log.info("Дневная гидратация получена | userId={}", userId);
        return dailyHydration;
    }
}
