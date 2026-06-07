CREATE TABLE body_weights (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    weight_kg NUMERIC(5,1) NOT NULL,
    measured_at DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE INDEX idx_body_weights_user_date ON body_weights(user_id, measured_at DESC);
