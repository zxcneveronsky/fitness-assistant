package com.example.fitness_assistant.application.service.hydration;

import com.example.fitness_assistant.core.exception.HydrationNotFoundException;
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
public class UpdateHydrationUseCase {

    private final UserRepository userRepository;
    private final HydrationRepository hydrationRepository;

    @Transactional
    public Hydration updateHydration(UserDetails userDetails, Hydration hydrationUpdate) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Long hydrationId = hydrationUpdate.getId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Hydration updatedHydration = hydrationRepository.findById(hydrationId, userId)
                .map(existingHydration -> {
                    existingHydration.setName(hydrationUpdate.getName() != null ? hydrationUpdate.getName() : existingHydration.getName());
                    existingHydration.setAmount(hydrationUpdate.getAmount() != null ? hydrationUpdate.getAmount() : existingHydration.getAmount());
                    existingHydration.setConsumedAt(hydrationUpdate.getConsumedAt() != null ? hydrationUpdate.getConsumedAt() : existingHydration.getConsumedAt());
                    return hydrationRepository.save(existingHydration);
                })
                .orElseThrow(() -> new HydrationNotFoundException(hydrationId));
        log.info("Запись гидратации обновлена | id={}", updatedHydration.getId());
        return updatedHydration;
    }
}
