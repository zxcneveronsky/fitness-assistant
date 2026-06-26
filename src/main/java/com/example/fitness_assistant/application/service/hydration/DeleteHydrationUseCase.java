package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.HydrationNotFoundException;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteHydrationUseCase {

    private final HydrationRepository hydrationRepository;

    @Transactional
    public void deleteHydration(Long hydrationId, Long userId) {
        if (!hydrationRepository.existsById(hydrationId, userId)) {
            throw new HydrationNotFoundException(hydrationId);
        }
        hydrationRepository.deleteById(hydrationId, userId);
        log.info("Запись гидратации удалена | id={}", hydrationId);
    }
}
