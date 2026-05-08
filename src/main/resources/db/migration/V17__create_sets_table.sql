CREATE TABLE IF NOT EXISTS sets (
    id          BIGSERIAL PRIMARY KEY,
    session_id  BIGINT NOT NULL REFERENCES workout_sessions(id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL,
    weight      DOUBLE PRECISION NOT NULL,
    reps        INTEGER NOT NULL,
    created_at  TIMESTAMP
);