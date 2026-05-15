package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.HydrationNotFoundException;
import com.example.fitness_assistant.core.model.hydration.Hydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindHydrationUseCase {

    private final HydrationRepository hydrationRepository;

    public Page<Hydration> findHydration(LocalDateTime localDateTime, UserDetails userDetails, Pageable pageable) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Page<Hydration> hydrations = hydrationRepository.searchHydration(localDateTime, userId, pageable);
        log.info("Поиск гидратации завершён | userId={} | найдено={} | страница={}/{}",
                userId, hydrations.getTotalElements(), hydrations.getNumber() + 1, hydrations.getTotalPages());
        return hydrations;
    }

    @Transactional
    public Hydration findById(Long id, UserDetails userDetails){
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Hydration hydration = hydrationRepository.findById(id,userId).orElseThrow(()->new HydrationNotFoundException(id));
        log.info("Запись гидратации найдена | id={}", id);
        return hydration;
    }
}
