CREATE TABLE IF NOT EXISTS users_profiles (
    user_id    BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    name       VARCHAR(255),
    birth_date DATE,
    weight     NUMERIC(5,1),
    height     DOUBLE PRECISION,
    gender     VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE'))
);
