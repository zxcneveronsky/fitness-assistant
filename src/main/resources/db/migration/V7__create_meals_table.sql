CREATE TABLE IF NOT EXISTS meals (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    name        VARCHAR(255) NOT NULL,
    brands      VARCHAR(255),
    kcal        DOUBLE PRECISION NOT NULL,
    proteins    DOUBLE PRECISION NOT NULL,
    fats        DOUBLE PRECISION NOT NULL,
    carbs       DOUBLE PRECISION NOT NULL,
    consumed_at DATE,

        CONSTRAINT fk_meals_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);