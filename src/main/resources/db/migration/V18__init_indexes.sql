CREATE INDEX IF NOT EXISTS idx_food_name_lower     ON foods      (LOWER(name));
CREATE INDEX IF NOT EXISTS idx_food_brands_lower   ON foods      (LOWER(brands));
CREATE INDEX IF NOT EXISTS idx_exercise_name_lower ON exercises  (LOWER(name));
CREATE INDEX IF NOT EXISTS idx_muscle_name_lower   ON muscles    (LOWER(name));
CREATE INDEX IF NOT EXISTS idx_meals_user_id       ON meals(user_id);
CREATE INDEX IF NOT EXISTS idx_meals_consumed_at   ON meals(consumed_at);
CREATE INDEX IF NOT EXISTS idx_meals_user_date     ON meals(user_id, consumed_at);

CREATE INDEX IF NOT EXISTS idx_workouts_user_id               ON workouts(user_id);
CREATE INDEX IF NOT EXISTS idx_workouts_name_lower            ON workouts (LOWER(name));
CREATE INDEX IF NOT EXISTS idx_workout_exercises_workout_id   ON workout_exercises(workout_id);
CREATE INDEX IF NOT EXISTS idx_workout_exercises_exercise_id  ON workout_exercises(exercise_id);

CREATE INDEX IF NOT EXISTS idx_workout_sessions_user_id    ON workout_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_workout_sessions_workout_id ON workout_sessions(workout_id);

CREATE INDEX IF NOT EXISTS idx_sets_session_id        ON sets(session_id);
CREATE INDEX IF NOT EXISTS idx_sets_exercise_id       ON sets(exercise_id);
CREATE INDEX IF NOT EXISTS idx_sets_session_exercise  ON sets(session_id, exercise_id);

CREATE INDEX IF NOT EXISTS idx_exercise_muscles_muscle_id ON exercise_muscles(muscle_id);

CREATE INDEX IF NOT EXISTS idx_hydrations_user_id      ON hydrations(user_id);
CREATE INDEX IF NOT EXISTS idx_hydrations_consumed_at  ON hydrations(consumed_at);
CREATE INDEX IF NOT EXISTS idx_hydrations_user_date    ON hydrations(user_id, consumed_at);

CREATE INDEX IF NOT EXISTS idx_body_weights_user_date ON body_weights(user_id, measured_at DESC);

CREATE INDEX IF NOT EXISTS idx_workout_access_shared_with_user ON workout_access(shared_with_user_id);
