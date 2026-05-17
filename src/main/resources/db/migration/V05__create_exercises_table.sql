CREATE TABLE IF NOT EXISTS exercises (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);