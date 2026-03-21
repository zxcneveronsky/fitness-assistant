package com.example.fitness_assistant.service;


import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import com.example.fitness_assistant.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.repository.ExerciseMuscleRepository;
import com.example.fitness_assistant.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
        List<ExerciseDTO.MuscleDTO> muscles = exerciseMuscleRepository
                .findByExercise_ExerciseNameIgnoreCase(exercise.getExerciseName())
                .stream()
                .map(m -> new ExerciseDTO.MuscleDTO(m.getMuscleGroup(), m.getMuscleDetail()))
                .toList();
        return new ExerciseDTO(exercise.getExerciseName(), exercise.getDescription(), muscles);
    }

    // Оставили только пагинацию
    public Page<ExerciseDTO> getAllExercisePaged(Pageable pageable) {
        Page<Exercise> page = exerciseRepository.findAll(pageable);
        if (page.isEmpty()) {
            log.warn("Страница {} пуста", pageable.getPageNumber());
            throw new ExerciseNotFoundException("Ничего");
        }
        return page.map(this::toDTO);
    }

    public List<ExerciseDTO> getExercisesByMuscle(String muscle) {
        List<ExerciseMuscle> muscles = exerciseMuscleRepository.findByMuscleGroupIgnoreCaseOrMuscleDetailIgnoreCase(muscle, muscle);
        if (muscles.isEmpty()) {
            log.warn("Не найдено упражнений для мышцы: {}", muscle);
            throw new ExerciseNotFoundException(muscle);
        }
        return muscles.stream()
                .map(ExerciseMuscle::getExercise)
                .distinct()
                .map(this::toDTO)
                .toList();
    }

    public ExerciseDTO getExerciseByName(String exerciseName) {
        Exercise exercise = exerciseRepository
                .findByExerciseNameIgnoreCase(exerciseName)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseName));
        return toDTO(exercise);
    }
    public Page<ExerciseDTO> searchExercisesByName(String name, Pageable pageable) {
        Page<Exercise> exercisePage = exerciseRepository.findByExerciseNameContainingIgnoreCase(name, pageable);

        return exercisePage.map(ex -> new ExerciseDTO(
                ex.getExerciseName(),
                ex.getDescription(),
                ex.getMuscles().stream()
                        .map(m -> new ExerciseDTO.MuscleDTO(m.getMuscleGroup(), m.getMuscleDetail()))
                        .toList()
        ));
    }

    @Transactional
    public void deleteExercise(String exerciseName) {
        Exercise exercise = exerciseRepository
                .findByExerciseNameIgnoreCase(exerciseName)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseName));
        exerciseMuscleRepository.deleteByExercise(exercise);
        exerciseRepository.delete(exercise);
        log.info("Упражнение {} удалено", exerciseName);
    }

    @Transactional
    public Exercise addExercise(ExerciseDTO exercise) {
        Exercise savedEx = new Exercise();
        savedEx.setExerciseName(exercise.exerciseName());
        savedEx.setDescription(exercise.description());
        Exercise savedExercise = exerciseRepository.save(savedEx);

        List<ExerciseMuscle> savedMuscles = exercise.muscles().stream()
                .map(m -> {
                    ExerciseMuscle em = new ExerciseMuscle();
                    em.setExercise(savedExercise);
                    em.setMuscleGroup(m.muscleGroup());
                    em.setMuscleDetail(m.muscleDetail());
                    return em;
                }).toList();
        exerciseMuscleRepository.saveAll(savedMuscles);
        return savedExercise;
    }
}