CREATE INDEX IF NOT EXISTS idx_workouts_user_id ON workouts(user_id);
CREATE INDEX IF NOT EXISTS idx_workouts_name_lower ON workouts (LOWER(name));
CREATE INDEX IF NOT EXISTS idx_workout_exercises_workout_id ON workout_exercises(workout_id);

CREATE INDEX IF NOT EXISTS idx_exercise_muscles_muscle_id ON exercise_muscles(muscle_id);

CREATE INDEX IF NOT EXISTS idx_hydrations_user_id ON hydrations(user_id);
CREATE INDEX IF NOT EXISTS idx_hydrations_consumed_at ON hydrations(consumed_at);