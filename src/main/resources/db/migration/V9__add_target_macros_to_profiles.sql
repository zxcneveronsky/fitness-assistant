ALTER TABLE users_profiles
ADD COLUMN target_kcal        DOUBLE PRECISION,
ADD COLUMN target_proteins    DOUBLE PRECISION,
ADD COLUMN target_fats        DOUBLE PRECISION,
ADD COLUMN target_carbs       DOUBLE PRECISION,
ADD COLUMN use_autopilot      BOOLEAN DEFAULT TRUE;
