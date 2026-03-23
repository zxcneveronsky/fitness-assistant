package com.example.fitness_assistant.service;

import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import com.example.fitness_assistant.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.repository.ExerciseMuscleRepository;
import com.example.fitness_assistant.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Cacheable(value = "exercises", key = "#pageable.pageNumber")
    public Page<ExerciseDTO> getAllExercisePaged(Pageable pageable) {
        return exerciseRepo.findAll(pageable).map(this::toDTO);
    }

    @Cacheable(value = "exercise", key = "#id")
    public ExerciseDTO getExerciseById(Long id) {
        return exerciseRepo.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ExerciseNotFoundException("ID: " + id));
    }

    @Cacheable(value = "exercisesSearch", key = "#query.toLowerCase() + '-' + #pageable.pageNumber")
    public Page<ExerciseDTO> searchExercises(String query, Pageable pageable) {
        Page<Exercise> byMuscle = muscleRepo.findDistinctExercisesByMuscle(query, pageable);
        if (!byMuscle.isEmpty()) return byMuscle.map(this::toDTO);
        return exerciseRepo.findByExerciseNameContainingIgnoreCase(query, pageable).map(this::toDTO);
    }

    @Cacheable(value = "exercisesByMuscle", key = "#muscle + '-' + #pageable.pageNumber")
    public Page<ExerciseDTO> getExercisesByMuscle(String muscle, Pageable pageable) {
        return muscleRepo.findDistinctExercisesByMuscle(muscle, pageable).map(this::toDTO);
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercise", "exercisesSearch", "exercisesByMuscle"}, allEntries = true)
    public ExerciseDTO addExercise(ExerciseDTO dto) {
        Exercise e = new Exercise();
        e.setExerciseName(dto.exerciseName());
        e.setDescription(dto.description());
        Exercise saved = exerciseRepo.save(e);
        saveMuscles(dto.muscles(), saved);
        return getExerciseById(saved.getId());
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercise", "exercisesSearch", "exercisesByMuscle"}, allEntries = true)
    public ExerciseDTO updateExercise(Long id, ExerciseDTO dto) {
        Exercise e = exerciseRepo.findById(id).orElseThrow(() -> new ExerciseNotFoundException("ID: " + id));
        e.setExerciseName(dto.exerciseName());
        e.setDescription(dto.description());
        muscleRepo.deleteByExercise(e);
        saveMuscles(dto.muscles(), e);
        return toDTO(exerciseRepo.save(e));
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercise", "exercisesSearch", "exercisesByMuscle"}, allEntries = true)
    public void deleteExercise(Long id) {
        if (!exerciseRepo.existsById(id)) throw new ExerciseNotFoundException("ID: " + id);
        exerciseRepo.deleteById(id);
    }

    private void saveMuscles(List<ExerciseDTO.MuscleDTO> dtos, Exercise e) {
        var list = dtos.stream().map(d -> {
            ExerciseMuscle em = new ExerciseMuscle();
            em.setExercise(e);
            em.setMuscleGroup(d.muscleGroup());
            em.setMuscleDetail(d.muscleDetail());
            return em;
        }).toList();
        muscleRepo.saveAll(list);
    }
}