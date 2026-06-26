package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.repository.TargetsRepository;
import com.example.fitness_assistant.infrastructure.mapper.TargetsMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.TargetsEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaTargetsRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TargetsRepositoryAdapter implements TargetsRepository {

    private final JpaTargetsRepository jpaTargetsRepository;
    private final JpaUserProfileRepository jpaUserProfileRepository;
    private final TargetsMapper targetsMapper;

    @Override
    public Optional<Targets> findById(Long profileId) {
        return jpaTargetsRepository.findById(profileId)
                .map(targetsMapper::toDomain);
    }

    @Override
    public Targets save(Targets targets) {
        TargetsEntity targetsEntity = targetsMapper.toEntity(targets);
        targetsEntity.setProfile(jpaUserProfileRepository.getReferenceById(targets.getProfileId()));
        TargetsEntity saved = jpaTargetsRepository.save(targetsEntity);
        return targetsMapper.toDomain(saved);
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
