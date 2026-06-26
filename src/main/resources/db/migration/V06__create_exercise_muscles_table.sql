CREATE TABLE IF NOT EXISTS exercise_muscles (
    exercise_id BIGINT NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    muscle_id   BIGINT NOT NULL REFERENCES muscles(id) ON DELETE CASCADE,
    PRIMARY KEY (exercise_id, muscle_id)
);