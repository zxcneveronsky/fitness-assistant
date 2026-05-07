package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindExerciseUseCase {

    private final ExerciseRepository exerciseRepository;

    public Page<Exercise> findExercise(String name, Pageable pageable) {
        return exerciseRepository.searchExercise(name, pageable);
    }

    @Transactional
    public Exercise findById(Long id){
        return exerciseRepository.findById(id).orElseThrow(()->new ExerciseNotFoundException(id));
    }
}
