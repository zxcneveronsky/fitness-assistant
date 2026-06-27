package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.HydrationNotFoundException;
import com.example.fitness_assistant.core.model.hydration.Hydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindHydrationUseCase {

    private final HydrationRepository hydrationRepository;

    @Transactional(readOnly = true)
    public Page<Hydration> searchHydration(Long userId, LocalDateTime consumedAt, Pageable pageable) {
        Page<Hydration> hydrations = hydrationRepository.searchHydration(userId, consumedAt, pageable);
        log.info("Поиск гидратации завершён | userId={} | найдено={} | страница={}/{}",
                userId, hydrations.getTotalElements(), hydrations.getNumber() + 1, hydrations.getTotalPages());
        return hydrations;
    }

    @Transactional(readOnly = true)
    public Hydration findById(Long userId, Long hydrationId){
        Hydration hydration = hydrationRepository.findById(hydrationId,userId).orElseThrow(()->new HydrationNotFoundException(hydrationId));
        log.info("Запись гидратации найдена | id={}", hydrationId);
        return hydration;
    }
}
