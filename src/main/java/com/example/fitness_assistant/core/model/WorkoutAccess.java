package com.example.fitness_assistant.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutAccess {
    private Long id;
    private Long ownerId;
    private Long sharedWithUserId;
    private String sharedWithUserEmail;
    private Long workoutId;
    private String workoutName;
    private AccessLevel accessLevel;
    public enum AccessLevel {
        READ, COPY
    }

}
