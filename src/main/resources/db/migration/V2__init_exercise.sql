CREATE TABLE IF NOT EXISTS exercise (
    id            BIGSERIAL PRIMARY KEY,
    exercise_name VARCHAR(255) NOT NULL,
    description   TEXT
);

CREATE TABLE IF NOT EXISTS exercise_muscle (
    id           BIGSERIAL PRIMARY KEY,
    exercise_id  BIGINT       NOT NULL REFERENCES exercise (id),
    muscle_group VARCHAR(100) NOT NULL,
    muscle_detail VARCHAR(100) NOT NULL
);