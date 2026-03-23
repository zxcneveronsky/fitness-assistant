package com.example.fitness_assistant.repository;

import com.example.fitness_assistant.entity.AppMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppMetadataRepository extends JpaRepository<AppMetadata, String> {
}