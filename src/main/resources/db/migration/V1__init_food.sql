CREATE TABLE IF NOT EXISTS food (
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255)     NOT NULL,
    brands   VARCHAR(255),
    kcal     DOUBLE PRECISION,
    proteins DOUBLE PRECISION,
    fats     DOUBLE PRECISION,
    carbs    DOUBLE PRECISION
);