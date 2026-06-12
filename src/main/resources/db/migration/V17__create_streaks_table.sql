CREATE TABLE streaks (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    streak INT DEFAULT 0,
    last_visit_date DATE
);
