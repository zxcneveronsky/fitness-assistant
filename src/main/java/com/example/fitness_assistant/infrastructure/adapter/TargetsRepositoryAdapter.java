package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import com.example.fitness_assistant.infrastructure.mapper.TargetsMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.TargetsEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.UserProfileEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaTargetsRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TargetsRepositoryAdapter implements TargetsRepository {

    private final JpaTargetsRepository jpaTargetsRepository;
    private final TargetsMapper targetsMapper;
    private final EntityManager entityManager;

    @Override
    public Optional<Targets> findById(Long profileId) {
        return jpaTargetsRepository.findById(profileId)
                .map(targetsMapper::toDomain);
    }

    @Override
    @Transactional
    public Targets save(Targets targets) {
        TargetsEntity targetsEntity = targetsMapper.toEntity(targets);
        targetsEntity.setProfile(entityManager.getReference(UserProfileEntity.class, targets.getProfileId()));
        if (!jpaTargetsRepository.existsById(targets.getProfileId())) {
            entityManager.persist(targetsEntity);
            return targetsMapper.toDomain(targetsEntity);
        } else {
            TargetsEntity merged = entityManager.merge(targetsEntity);
            return targetsMapper.toDomain(merged);
        }
    }

    @Override
    public void deleteById(Long profileId) {
        jpaTargetsRepository.deleteById(profileId);
    }

    @Override
    public boolean existsById(Long profileId) {
        return jpaTargetsRepository.existsById(profileId);
    }
}
