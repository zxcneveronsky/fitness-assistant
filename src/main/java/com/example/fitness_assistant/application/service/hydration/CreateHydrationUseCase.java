package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.hydration.Hydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateHydrationUseCase {

    private final HydrationRepository hydrationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Hydration createHydration(Long userId, Hydration hydration) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Hydration newHydration = new Hydration(
                null,
                userId,
                hydration.getName(),
                hydration.getAmount(),
                hydration.getConsumedAt()
        );
        Hydration savedHydration = hydrationRepository.save(newHydration);
        log.info("Запись гидратации создана | id={} | название='{}'", savedHydration.getId(), savedHydration.getName());
        return savedHydration;
    }
}
