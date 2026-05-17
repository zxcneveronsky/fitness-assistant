CREATE TABLE IF NOT EXISTS targets (
    profile_id       BIGINT PRIMARY KEY REFERENCES users_profiles(user_id) ON DELETE CASCADE,
    target_kcal      DOUBLE PRECISION,
    target_proteins  DOUBLE PRECISION,
    target_fats      DOUBLE PRECISION,
    target_carbs     DOUBLE PRECISION,
    target_hydration DOUBLE PRECISION,
    use_autopilot    BOOLEAN DEFAULT TRUE
);