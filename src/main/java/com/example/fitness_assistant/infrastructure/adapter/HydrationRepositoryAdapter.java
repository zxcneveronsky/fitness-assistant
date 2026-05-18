package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.hydration.DailyHydration;
import com.example.fitness_assistant.core.model.hydration.Hydration;
import com.example.fitness_assistant.core.repository.HydrationRepository;
import com.example.fitness_assistant.infrastructure.mapper.DailyHydrationMapper;
import com.example.fitness_assistant.infrastructure.mapper.HydrationMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.HydrationEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaHydrationRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HydrationRepositoryAdapter implements HydrationRepository {

    private final JpaHydrationRepository jpaHydrationRepository;
    private final JpaUserRepository jpaUserRepository;
    private final HydrationMapper hydrationMapper;
    private final DailyHydrationMapper dailyHydrationMapper;

    @Override
    public Optional<Hydration> findById(Long id, Long userId) {
        return jpaHydrationRepository.findByIdAndUserId(id, userId)
                .map(hydrationMapper::toDomain);
    }

    @Override
    public Page<Hydration> searchHydration(LocalDateTime localDateTime, Long userId, Pageable pageable) {
        LocalDate date = localDateTime != null ? localDateTime.toLocalDate() : LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return jpaHydrationRepository.searchHydration(userId, startOfDay, endOfDay, pageable)
                .map(hydrationMapper::toDomain);
    }

    @Override
    public DailyHydration getDailyWater(LocalDateTime localDateTime, Long userId) {
        LocalDate date = localDateTime != null ? localDateTime.toLocalDate() : LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return dailyHydrationMapper.toDomain(jpaHydrationRepository.getDailyWater(userId, startOfDay, endOfDay));
    }


    @Override
    public Hydration save(Hydration hydration) {
        Long userId = hydration.getUserId();
        HydrationEntity hydrationEntity = hydrationMapper.toEntity(hydration);
        hydrationEntity.setUser(jpaUserRepository.getReferenceById(userId));
        return hydrationMapper.toDomain(jpaHydrationRepository.save(hydrationEntity));
    }

    @Override
    public void deleteById(Long id, Long userId) {
        jpaHydrationRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsById(Long id, Long userId) {
        return jpaHydrationRepository.existsByIdAndUserId(id, userId);
    }
}
