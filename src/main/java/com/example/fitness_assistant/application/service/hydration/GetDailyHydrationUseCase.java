package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.model.hydration.DailyHydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetDailyHydrationUseCase {
    private final HydrationRepository hydrationRepository;

    @Transactional(readOnly = true)
    public DailyHydration getDailyHydration(Long userId, LocalDateTime consumedAt) {
        DailyHydration dailyHydration = hydrationRepository.getDailyHydration(userId, consumedAt);
        log.info("Дневная гидратация получена | userId={} | consumedAt={}", userId, consumedAt);
        return dailyHydration;
    }
}
