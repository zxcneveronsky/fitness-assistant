-- GIN-индексы с pg_trgm для поиска "%текст%".
-- Btree-индексы из V18 не работают для LIKE '%...%', trigram — работают.

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_foods_name_trgm   ON foods     USING gin (LOWER(name) gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_foods_brands_trgm ON foods     USING gin (LOWER(brands) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_exercises_name_trgm ON exercises USING gin (LOWER(name) gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_muscles_name_trgm   ON muscles    USING gin (LOWER(name) gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_workouts_name_trgm  ON workouts   USING gin (LOWER(name) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_exercises_name ON exercises (name);
CREATE INDEX IF NOT EXISTS idx_foods_name     ON foods     (name);

CREATE INDEX IF NOT EXISTS idx_workout_sessions_user_start ON workout_sessions(user_id, start_time DESC);
CREATE INDEX IF NOT EXISTS idx_workout_access_workout      ON workout_access(workout_id);
