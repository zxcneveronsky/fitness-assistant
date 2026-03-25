package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import com.example.fitness_assistant.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.repository.ExerciseMuscleRepository;
import com.example.fitness_assistant.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepo;
    private final ExerciseMuscleRepository muscleRepo;

    private ExerciseDTO toDTO(Exercise e) {
        var muscles = e.getMuscles().stream()
                .map(m -> new ExerciseDTO.MuscleDTO(m.getMuscleGroup(), m.getMuscleDetail()))
                .toList();
        return new ExerciseDTO(e.getId(), e.getExerciseName(), e.getDescription(), muscles);
    }

    private Exercise toEntity(ExerciseDTO dto) {
        Exercise exercise = new Exercise();
        exercise.setExerciseName(dto.exerciseName());
        exercise.setDescription(dto.description() != null ? dto.description() : "");
        return exercise;
    }

    @Cacheable(value = "exercises", key = "#pageable.pageNumber")
    public Page<ExerciseDTO> getAllExercisePaged(Pageable pageable) {
        return exerciseRepo.findAll(pageable).map(this::toDTO);
    }

    @Cacheable(value = "exercise", key = "#id")
    public ExerciseDTO getExerciseById(Long id) {
        return exerciseRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ExerciseNotFoundException("ID: " + id));
    }

    @Cacheable(value = "exercisesSearch", key = "#query.toLowerCase() + '-' + #pageable.pageNumber")
    public Page<ExerciseDTO> searchExercises(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        Page<Exercise> exercises = exerciseRepo.searchExercises(query, pageable);
        return exercises.map(this::toDTO);
    }

    @Cacheable(value = "exercisesByMuscle", key = "#muscle.toLowerCase() + '-' + #pageable.pageNumber")
    public Page<ExerciseDTO> getExercisesByMuscle(String muscle, Pageable pageable) {
        return muscleRepo.findDistinctExercisesByMuscle(muscle, pageable).map(this::toDTO);
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercise", "exercisesSearch", "exercisesByMuscle"}, allEntries = true)
    public ExerciseDTO addExercise(ExerciseDTO dto) {
        Exercise exercise = toEntity(dto);
        Exercise saved = exerciseRepo.save(exercise);
        saveMuscles(dto.muscles(), saved);
        log.info("Добавлено упражнение: {}", saved.getExerciseName());
        return toDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercise", "exercisesSearch", "exercisesByMuscle"}, allEntries = true)
    public ExerciseDTO updateExercise(Long id, ExerciseDTO dto) {
        Exercise exercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("ID: " + id));
        exercise.setExerciseName(dto.exerciseName());
        exercise.setDescription(dto.description() != null ? dto.description() : "");
        muscleRepo.deleteByExercise(exercise);
        saveMuscles(dto.muscles(), exercise);
        Exercise saved = exerciseRepo.save(exercise);
        log.info("Обновлено упражнение: {} (id={})", saved.getExerciseName(), id);
        return toDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercise", "exercisesSearch", "exercisesByMuscle"}, allEntries = true)
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("ID: " + id));
        muscleRepo.deleteByExercise(exercise);
        exerciseRepo.deleteById(id);
        log.info("Удалено упражнение с id: {}", id);
    }


    private void saveMuscles(List<ExerciseDTO.MuscleDTO> dtos, Exercise exercise) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }
        var muscleEntities = dtos.stream().map(dto -> {
            ExerciseMuscle em = new ExerciseMuscle();
            em.setExercise(exercise);
            em.setMuscleGroup(dto.muscleGroup());
            em.setMuscleDetail(dto.muscleDetail() != null ? dto.muscleDetail() : "");
            return em;
        }).toList();
        muscleRepo.saveAll(muscleEntities);
    }
}