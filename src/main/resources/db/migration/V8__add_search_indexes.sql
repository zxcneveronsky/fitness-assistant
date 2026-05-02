CREATE INDEX idx_food_name_lower     ON foods      (LOWER(name));
CREATE INDEX idx_food_brands_lower   ON foods      (LOWER(brands));
CREATE INDEX idx_exercise_name_lower ON exercises (LOWER(name));
CREATE INDEX idx_muscle_name_lower   ON muscles   (LOWER(name));
CREATE INDEX idx_meals_user_id       ON meals(user_id);
CREATE INDEX idx_meals_consumed_at   ON meals(consumed_at);