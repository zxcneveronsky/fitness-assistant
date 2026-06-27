CREATE TABLE IF NOT EXISTS workout_access (
    id                  BIGSERIAL   PRIMARY KEY,
    owner_id            BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    shared_with_user_id BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    workout_id          BIGINT      NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    access_level        VARCHAR(16) NOT NULL CHECK (access_level IN ('READ', 'COPY')),
    UNIQUE (owner_id, shared_with_user_id, workout_id)
);
