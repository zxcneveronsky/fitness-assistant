CREATE TABLE IF NOT EXISTS meals (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name        VARCHAR(255) NOT NULL,
    brands      VARCHAR(255),
    kcal        DOUBLE PRECISION NOT NULL,
    proteins    DOUBLE PRECISION NOT NULL,
    fats        DOUBLE PRECISION NOT NULL,
    carbs       DOUBLE PRECISION NOT NULL,
    consumed_at TIMESTAMP
);