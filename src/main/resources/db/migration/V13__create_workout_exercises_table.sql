CREATE TABLE IF NOT EXISTS workout_exercises (
    workout_id  BIGINT NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL
);