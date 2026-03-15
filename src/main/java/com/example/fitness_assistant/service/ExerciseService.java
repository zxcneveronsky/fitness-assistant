package com.example.fitness_assistant.service;


import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    public ExerciseService(ExerciseRepository exerciseRepository){
        this.exerciseRepository = exerciseRepository; }

    private List<ExerciseDTO> toDTO(List<Exercise> ex){
        return ex.stream()
                .map(e -> new ExerciseDTO(
                        e.getExerciseName(),
                        e.getDescription(),
                        List.of(new ExerciseDTO.MuscleDTO(e.getMuscleGroup(),e.getMuscleDetail()))
                ))
                .toList();
    }

    public List<ExerciseDTO> getAllExercise(){
        List<Exercise> exercises = exerciseRepository.findAll();
        if (exercises.isEmpty()){
            throw new ExerciseNotFoundException("Ничего");
        }
        return toDTO(exercises);
    }

    public List<ExerciseDTO> getExerciseName(String muscle){
        List<Exercise> exercisesName = exerciseRepository.findByMuscleGroupOrMuscleDetail(muscle,muscle);
        if (exercisesName.isEmpty()){
            throw new ExerciseNotFoundException(muscle);
        }
        return toDTO(exercisesName);
    }

    public ExerciseDTO getMuscleName(String exerciseName){
        List<Exercise> muscleName = exerciseRepository.findByExerciseName(exerciseName);
        if (muscleName.isEmpty()){
            throw new ExerciseNotFoundException(exerciseName);
        }
        return new ExerciseDTO(
                muscleName.get(0).getExerciseName(),
                muscleName.get(0).getDescription(),
                muscleName.stream()
                        .map(m -> new ExerciseDTO.MuscleDTO(m.getMuscleGroup(),m.getMuscleDetail()))
                        .toList()
        );
    }
}
