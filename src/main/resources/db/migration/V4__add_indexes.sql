CREATE INDEX idx_food_name_lower ON food (LOWER(name));
CREATE INDEX idx_food_brands_lower ON food (LOWER(brands));
CREATE INDEX idx_exercise_name_lower ON exercise (LOWER(exercise_name));