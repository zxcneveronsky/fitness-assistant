CREATE TABLE IF NOT EXISTS workout_exercises (
    workout_id      BIGINT NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_id     BIGINT NOT NULL REFERENCES exercises(id) ON DELETE RESTRICT,
    exercise_order  INTEGER NOT NULL,
    PRIMARY KEY (workout_id, exercise_id)
);