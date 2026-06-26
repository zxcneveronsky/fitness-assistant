CREATE TABLE IF NOT EXISTS body_weights (
    id          BIGSERIAL    PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    weight      NUMERIC(5,1) NOT NULL,
    measured_at DATE         NOT NULL DEFAULT CURRENT_DATE
);
