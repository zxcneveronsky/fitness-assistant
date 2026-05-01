CREATE TABLE IF NOT EXISTS users_profiles (
    user_id    BIGINT PRIMARY KEY REFERENCES users(id),
    name       VARCHAR(255),
    birth_date DATE,
    weight     DOUBLE PRECISION,
    height     DOUBLE PRECISION,
    gender     VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER'))
);