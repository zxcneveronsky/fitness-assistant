CREATE TABLE IF NOT EXISTS favorite_exercises (
    id          BIGSERIAL   PRIMARY KEY,
    user_id     BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    exercise_id BIGINT      NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    UNIQUE (user_id, exercise_id)
);

CREATE TABLE IF NOT EXISTS favorite_foods (
    id      BIGSERIAL   PRIMARY KEY,
    user_id BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    food_id BIGINT      NOT NULL REFERENCES foods(id) ON DELETE CASCADE,
    UNIQUE (user_id, food_id)
);
