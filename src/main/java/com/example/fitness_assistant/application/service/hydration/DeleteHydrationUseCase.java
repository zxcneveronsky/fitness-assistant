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
    public void deleteHydration(Long id, Long userId) {
        if (!hydrationRepository.existsById(id, userId)) {
            throw new HydrationNotFoundException(id);
        }
        hydrationRepository.deleteById(id, userId);
        log.info("Запись гидратации удалена | id={}", id);
    }
}
