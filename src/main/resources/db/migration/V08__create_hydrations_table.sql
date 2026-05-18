CREATE TABLE IF NOT EXISTS hydrations (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name        VARCHAR(255) NOT NULL,
    amount      DOUBLE PRECISION NOT NULL,
    consumed_at TIMESTAMP
);