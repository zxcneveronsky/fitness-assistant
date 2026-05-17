CREATE TABLE IF NOT EXISTS exercise_muscles (
    exercise_id BIGINT NOT NULL REFERENCES exercises(id),
    muscle_id   BIGINT NOT NULL REFERENCES muscles(id),
    PRIMARY KEY (exercise_id, muscle_id)
);