CREATE TABLE IF NOT EXISTS workout_sessions (
    id          BIGSERIAL PRIMARY KEY,
    workout_id  BIGINT NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    start_time  TIMESTAMP,
    end_time    TIMESTAMP
);