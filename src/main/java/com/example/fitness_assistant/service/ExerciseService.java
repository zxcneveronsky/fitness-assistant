package com.example.fitness_assistant.service;


import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import com.example.fitness_assistant.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.repository.ExerciseMuscleRepository;
import com.example.fitness_assistant.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMuscleRepository exerciseMuscleRepository;

    public ExerciseService(ExerciseRepository exerciseRepository,
                           ExerciseMuscleRepository exerciseMuscleRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMuscleRepository = exerciseMuscleRepository;
    }

    private ExerciseDTO toDTO(Exercise exercise) {
        List<ExerciseDTO.MuscleDTO> muscles = exercise.getMuscles().stream()
                .map(m -> new ExerciseDTO.MuscleDTO(m.getMuscleGroup(), m.getMuscleDetail()))
                .toList();
        return new ExerciseDTO(exercise.getExerciseName(), exercise.getDescription(), muscles);
    }
    @Cacheable(value = "exercises", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ExerciseDTO> getAllExercisePaged(Pageable pageable) {
        Page<Exercise> page = exerciseRepository.findAll(pageable);
        if (page.isEmpty()) {
            log.warn("Страница {} пуста", pageable.getPageNumber());
            throw new ExerciseNotFoundException("Ничего");
        }
        return page.map(this::toDTO);
    }
    @Cacheable(value = "exercisesByMuscle", key = "#muscle + '-' + #pageable.pageNumber")
    public Page<ExerciseDTO> getExercisesByMuscle(String muscle, Pageable pageable) {
        Page<Exercise> exercises = exerciseMuscleRepository
                .findDistinctExercisesByMuscle(muscle, pageable);
        if (exercises.isEmpty()) {
            log.warn("Не найдено упражнений для мышцы: {}", muscle);
            throw new ExerciseNotFoundException(muscle);
        }
        return exercises.map(this::toDTO);
    }
    @Cacheable(value = "exerciseByName", key = "#exerciseName.toLowerCase()")
    public ExerciseDTO getExerciseByName(String exerciseName) {
        Exercise exercise = exerciseRepository
                .findByExerciseNameIgnoreCase(exerciseName)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseName));
        return toDTO(exercise);
    }
    @Cacheable(value = "exercisesSearch", key = "#name.toLowerCase() + '-' + #pageable.pageNumber")
    public Page<ExerciseDTO> searchExercisesByName(String name, Pageable pageable) {
        Page<Exercise> byMuscle = exerciseMuscleRepository.findDistinctExercisesByMuscle(name, pageable);
        if (!byMuscle.isEmpty()) {
            return byMuscle.map(this::toDTO);
        }
        return exerciseRepository
                .findByExerciseNameContainingIgnoreCase(name, pageable)
                .map(this::toDTO);
    }
    @CacheEvict(value = {"exercises", "exercisesByMuscle", "exerciseByName", "exercisesSearch"}, allEntries = true)    @Transactional
    public void deleteExercise(String exerciseName) {
        Exercise exercise = exerciseRepository
                .findByExerciseNameIgnoreCase(exerciseName)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseName));
        exerciseMuscleRepository.deleteByExercise(exercise);
        exerciseRepository.delete(exercise);
        log.info("Упражнение {} удалено", exerciseName);
    }
    @CacheEvict(value = {"exercises", "exercisesByMuscle", "exerciseByName", "exercisesSearch"}, allEntries = true)
    @Transactional
    public ExerciseDTO addExercise(ExerciseDTO dto) {
        Exercise savedEx = new Exercise();
        savedEx.setExerciseName(dto.exerciseName());
        savedEx.setDescription(dto.description());
        Exercise savedExercise = exerciseRepository.save(savedEx);

        List<ExerciseMuscle> savedMuscles = dto.muscles().stream()
                .map(m -> {
                    ExerciseMuscle em = new ExerciseMuscle();
                    em.setExercise(savedExercise);
                    em.setMuscleGroup(m.muscleGroup());
                    em.setMuscleDetail(m.muscleDetail());
                    return em;
                }).toList();
        exerciseMuscleRepository.saveAll(savedMuscles);

        return toDTO(savedExercise); // возвращаем DTO, не Entity
    }
}